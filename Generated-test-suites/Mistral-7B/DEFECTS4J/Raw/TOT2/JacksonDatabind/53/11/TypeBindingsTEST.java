import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals("[]", bindings.toString());
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testSimpleTypeBindings() {
        Class<List<String>> listOfStrings = List.of(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStrings, JavaType.constructCollectionType(List.class, String.class));
        assertEquals("[Ljava/lang/String;", bindings.toString());
        assertEquals(String.class, bindings.findBoundType("T"));
    }

    @Test
    public void testTypeBindingsWithUnboundVariable() {
        Class<Map<String, List<Integer>>> mapOfListInt = Map.of(String.class, List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapOfListInt, JavaType.constructMapType(Map.class, String.class, JavaType.constructCollectionType(List.class, Integer.class)));
        assertEquals("{Ljava/lang/String;Ljava/util/List;[[I", bindings.toString());
        assertEquals("String", bindings.getBoundName(0));
        assertEquals("List", bindings.getBoundName(1));
        assertEquals(String.class, bindings.findBoundType("K"));
        assertEquals(Integer.class, bindings.findBoundType("V"));
        assertEquals(List.class, bindings.findBoundType("V").getComponentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeBindingsCreation() {
        Class<Map<String, List<Integer>>> mapOfListInt = Map.of(String.class, List.class, Integer.class);
        JavaType[] types = new JavaType[]{JavaType.constructCollectionType(List.class, String.class), JavaType.constructMapType(Map.class, String.class, JavaType.constructCollectionType(List.class, Integer.class))};
        TypeBindings bindings = new TypeBindings(new String[]{"K", "V"}, types, null);
        assertNull(bindings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeArguments() {
        Class<Map<String, List<Integer>>> mapOfListInt = Map.of(String.class, List.class, Integer.class);
        JavaType typeArg1 = JavaType.constructCollectionType(List.class, String.class);
        JavaType typeArg2 = JavaType.constructMapType(Map.class, String.class, JavaType.constructCollectionType(List.class, Integer.class));
        TypeBindings bindings = TypeBindings.create(mapOfListInt, typeArg1);
        assertNull(bindings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeArgumentOrder() {
        Class<Map<List<Integer>, String>> mapOfListString = Map.of(List.class, String.class);
        JavaType typeArg1 = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType typeArg2 = JavaType.constructMapType(Map.class, String.class, JavaType.constructCollectionType(List.class, Integer.class));
        TypeBindings bindings = TypeBindings.create(mapOfListString, typeArg2, typeArg1);
        assertNull(bindings);
    }
}