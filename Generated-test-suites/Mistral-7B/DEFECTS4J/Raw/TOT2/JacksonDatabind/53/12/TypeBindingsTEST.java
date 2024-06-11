import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterized;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals("[]", empty.toString());
    }

    @Test
    public void testCreateBindingsForOneTypeParameter() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings intListBindings = TypeBindings.create(intListClass, JavaType.constructParametricType(List.class, JavaType.of(Integer.class)));
        assertEquals(1, intListBindings.size());
        assertEquals("List<Integer>", intListBindings.toString());
        JavaType boundType = intListBindings.findBoundType("Integer");
        assertEquals(JavaType.of(Integer.class), boundType);
    }

    @Test
    public void testCreateBindingsForTwoTypeParameters() {
        Class<Map<Integer, List<String>>> mapListStringIntClass = new HashMap<Integer, ArrayList<String>>().getClass();
        TypeBindings mapListStringIntBindings = TypeBindings.create(mapListStringIntClass,
                JavaType.constructParametricType(Map.class, JavaType.constructTypeReference(Integer.class, List.class), JavaType.constructTypeReference(String.class, List.class)));
        assertEquals(2, mapListStringIntBindings.size());
        assertEquals("Map<Integer, List<String>>", mapListStringIntBindings.toString());
        JavaType keyBoundType = mapListStringIntBindings.findBoundType("Integer");
        assertEquals(JavaType.constructTypeReference(Integer.class), keyBoundType);
        JavaType valueBoundType = mapListStringIntBindings.findBoundType("List<String>");
        assertEquals(JavaType.constructParametricType(List.class, JavaType.of(String.class)), valueBoundType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForInvalidNumberOfTypeParameters() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings intListBindings = TypeBindings.create(intListClass, JavaType.constructParametricType(List.class, JavaType.of(Integer.class), JavaType.of(String.class)));
    }

    @Test
    public void testCreateBindingsForNullTypeList() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings intListBindings = TypeBindings.create(intListClass, null);
        assertTrue(intListBindings.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForInvalidErasedType() {
        Class<String> stringClass = String.class;
        TypeBindings stringBindings = TypeBindings.create(stringClass, JavaType.constructParametricType(List.class, JavaType.of(Integer.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForInvalidTypeList() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        JavaType[] invalidTypes = { JavaType.constructTypeReference(String.class), JavaType.constructParametricType(List.class, JavaType.of(Integer.class)) };
        TypeBindings intListBindings = TypeBindings.create(intListClass, invalidTypes);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings intListBindings = TypeBindings.create(List.class, JavaType.constructParametricType(List.class, JavaType.of(Integer.class)));
        TypeBindings withUnboundVariable = intListBindings.withUnboundVariable("T");
        assertTrue(withUnboundVariable.hasUnbound("T"));
        assertEquals(1 + intListBindings.size(), withUnboundVariable.size());
    }
}