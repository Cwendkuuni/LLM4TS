import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterized;
import org.junit.Test;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        assertTrue(emptyBindings.isEmpty());
        assertEquals(0, emptyBindings.size());
        assertNull(emptyBindings.findBoundType(""));
    }

    @Test
    public void testCreateWithOneType() {
        Class<List<String>> stringListClass = new ArrayList<String>().getClass();
        TypeVariable<String> stringTypeVariable = stringListClass.getTypeParameters()[0];
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(stringListClass, stringType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals(Arrays.asList(String.class), bindings.getTypeParameters());
    }

    @Test
    public void testCreateWithTwoTypes() {
        Class<Map<String, List<Integer>>> mapListClass = new HashMap<String, ArrayList<Integer>>().getClass();
        TypeVariable<String> keyTypeVariable = mapListClass.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueTypeVariable = mapListClass.getTypeParameters()[1];
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType listIntType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapListClass, stringType, listIntType);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(Arrays.asList(String.class, List.class, Integer.class), bindings.getTypeParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithTwoTypesAndTooFewTypeArguments() {
        Class<Map<String, List<Integer>>> mapListClass = new HashMap<String, ArrayList<Integer>>().getClass();
        TypeVariable<String> keyTypeVariable = mapListClass.getTypeParameters()[0];
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(mapListClass, stringType);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithTwoTypesAndTooManyTypeArguments() {
        Class<Map<String, List<Integer>>> mapListClass = new HashMap<String, ArrayList<Integer>>().getClass();
        TypeVariable<String> keyTypeVariable = mapListClass.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueTypeVariable = mapListClass.getTypeParameters()[1];
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType listIntType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType unboundType = TypeFactory.defaultInstance().constructUnboundedType(Object.class);
        TypeBindings bindings = TypeBindings.create(mapListClass, stringType, listIntType, unboundType);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("T");
        assertTrue(bindingsWithUnbound.hasUnbound("T"));
        assertEquals(1, bindingsWithUnbound.size());
        assertNull(bindingsWithUnbound.findBoundType("T"));
    }
}