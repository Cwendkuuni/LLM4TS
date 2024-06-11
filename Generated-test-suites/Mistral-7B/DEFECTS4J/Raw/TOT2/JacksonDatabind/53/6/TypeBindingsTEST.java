import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertEquals("<>", bindings.toString());
    }

    @Test
    public void testCreateSimpleBindings() {
        Class<List<Integer>> listClass = List.class.getTypeParameters()[0];
        JavaType javaType = JavaType.constructParameterizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(listClass, javaType);
        assertEquals(1, bindings.size());
        assertEquals("List", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0).getRawClass());
        assertEquals("java.util.List<java.lang.Integer>", bindings.toString());
    }

    @Test
    public void testCreateComplexBindings() {
        Class<Map<String, List<Integer>>> mapClass = Map.class.getTypeParameters()[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructParameterizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType);
        assertEquals(2, bindings.size());
        assertEquals("Map", bindings.getBoundName(0));
        assertEquals(Map.class, bindings.getBoundType(0).getRawClass());
        assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>", bindings.toString());
    }

    @Test
    public void testCreateIfNeededBindings() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class.getTypeParameters()[0];
        JavaType javaType = JavaType.constructParameterizedType(ArrayList.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(arrayListClass, javaType);
        assertEquals(1, bindings.size());
        assertEquals("List", bindings.getBoundName(0));
        assertEquals(ArrayList.class, bindings.getBoundType(0).getRawClass());
        assertEquals("java.util.List<java.lang.Integer>", bindings.toString());
    }

    @Test
    public void testFindBoundType() {
        String name = "List";
        JavaType javaType = JavaType.constructParameterizedType(List.class, Integer.class);
        TypeBindings bindings = new TypeBindings(new String[]{"List"}, new JavaType[]{javaType}, null);
        JavaType boundType = bindings.findBoundType(name);
        assertEquals(List.class, boundType.getRawClass());
    }

    @Test
    public void testFindBoundTypeNull() {
        String name = "Null";
        JavaType javaType = JavaType.constructParameterizedType(List.class, Integer.class);
        TypeBindings bindings = new TypeBindings(new String[]{"List"}, new JavaType[]{javaType}, null);
        assertNull(bindings.findBoundType(name));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());

        String[] names = new String[]{"List", "Map"};
        JavaType[] types = new JavaType[]{JavaType.constructParameterizedType(List.class, Integer.class), JavaType.constructParameterizedType(Map.class, String.class, List.class)};
        TypeBindings bindings2 = new TypeBindings(names, types, null);
        assertFalse(bindings2.isEmpty());
    }

    @Test
    public void testSize() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());

        String[] names = new String[]{"List", "Map"};
        JavaType[] types = new JavaType[]{JavaType.constructParameterizedType(List.class, Integer.class), JavaType.constructParameterizedType(Map.class, String.class, List.class)};
        TypeBindings bindings2 = new TypeBindings(names, types, null);
        assertEquals(2, bindings2.size());
    }

    @Test
    public void testGetBoundName() {
        String[] names = new String[]{"List", "Map"};
        JavaType[] types = new JavaType[]{JavaType.constructParameterizedType(List.class, Integer.class), JavaType.constructParameterizedType(Map.class, String.class, List.class)};
        TypeBindings bindings = new TypeBindings(names, types, null);
        assertEquals("List", bindings.getBoundName(0));
        assertEquals("Map", bindings.getBoundName(1));
    }

    @Test
    public void testGetBoundType() {
        String[] names = new String[]{"List", "Map"};
        JavaType[] types = new JavaType[]{JavaType.constructParameterizedType(List.class, Integer.class), JavaType.constructParameterizedType(Map.class, String.class, List.class)};
        TypeBindings bindings = new TypeBindings(names, types, null);
        assertEquals(JavaType.constructParameterizedType(List.class, Integer.class), bindings.getBoundType(0));
        assertEquals(JavaType.constructParameterizedType(Map.class, String.class, List.class), bindings.getBoundType(1));
    }

    @Test
    public void testGetTypeParameters() {
        JavaType javaType = JavaType.constructParameterizedType(Map.class, String.class, List.class);
        TypeBindings bindings = new TypeBindings(new String[]{"Map"}, new JavaType[]{javaType}, null);
        List<JavaType> javaTypes = bindings.getTypeParameters();
        assertEquals(2, javaTypes.size());
        assertEquals(JavaType.constructParameterizedType(String.class, null), javaTypes.get(0));
        assertEquals(JavaType.constructParameterizedType(List.class, Integer.class), javaTypes.get(1));
    }

    @Test
    public void testHasUnbound() {
        String[] names = new String[]{"List", "Map"};
        JavaType[] types = new JavaType[]{JavaType.constructParameterizedType(List.class, Integer.class), JavaType.constructParameterizedType(Map.class, String.class, List.class)};
        TypeBindings bindings = new TypeBindings(names, types, null);
        assertFalse(bindings.hasUnbound("List"));
        assertFalse(bindings.hasUnbound("Map"));

        String[] namesWithUnbound = new String[]{"List", "Map", "Unbound"};
        TypeBindings bindingsWithUnbound = new TypeBindings(namesWithUnbound, types, new String[]{"Unbound"});
        assertTrue(bindingsWithUnbound.hasUnbound("Unbound"));
    }
}