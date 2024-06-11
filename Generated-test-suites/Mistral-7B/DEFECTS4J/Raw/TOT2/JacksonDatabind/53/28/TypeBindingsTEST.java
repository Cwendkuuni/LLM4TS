import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterized;
import org.junit.Assert;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        Assert.assertNotNull(empty);
        Assert.assertEquals(0, empty.size());
        Assert.assertNull(empty.findBoundType("Test"));
    }

    @Test
    public void testCreateWithOneType() {
        Class<List<String>> clz = new ArrayList<String>().getClass();
        JavaType expectedType = JavaType.constructParametricType(clz, String.class);
        TypeBindings bindings = TypeBindings.create(clz, expectedType);
        Assert.assertEquals(1, bindings.size());
        Assert.assertEquals("java.lang.String", bindings.getBoundName(0));
        Assert.assertEquals(expectedType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypes() {
        Class<Map<String, List<String>>> clz = new HashMap<String, List<String>>().getClass();
        JavaType keyType = JavaType.constructParametricType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametricType(ArrayList.class, String.class);
        JavaType expectedType = new TypeParameterized.SimpleType(clz, keyType, valueType);
        TypeBindings bindings = TypeBindings.create(clz, keyType, valueType);
        Assert.assertEquals(2, bindings.size());
        Assert.assertEquals("java.lang.String", bindings.getBoundName(0));
        Assert.assertEquals(keyType, bindings.getBoundType(0));
        Assert.assertEquals("java.util.List", bindings.getBoundName(1));
        Assert.assertEquals(valueType, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreateWithTwoTypes() {
        Class<Map<String, List<Object>>> clz = new HashMap<String, List<Object>>().getClass();
        JavaType keyType = JavaType.constructParametricType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametricType(ArrayList.class, String.class);
        TypeBindings.create(clz, keyType, valueType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreateWithOneType() {
        Class<List<String>> clz = new ArrayList<String>().getClass();
        JavaType expectedType = JavaType.constructParametricType(clz, String.class);
        TypeBindings.create(clz, expectedType, new TypeReference<String>() {});
    }

    @Test
    public void testCreateIfNeeded() {
        Class<ArrayList<ArrayList<String>>> clz = new ArrayList<ArrayList<String>>().getClass();
        JavaType expectedType = JavaType.constructParametricType(clz, ArrayList.class, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clz, ArrayList.class, String.class);
        Assert.assertEquals(1, bindings.size());
        Assert.assertEquals("java.util.ArrayList", bindings.getBoundName(0));
        Assert.assertEquals(expectedType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithEmptyTypes() {
        Class<ArrayList<ArrayList<String>>> clz = new ArrayList<ArrayList<String>>().getClass();
        TypeBindings bindings = TypeBindings.createIfNeeded(clz, new TypeReference<ArrayList<ArrayList<String>>>() {});
        Assert.assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<Map<String, List<String>>> clz = new HashMap<String, List<String>>().getClass();
        JavaType keyType = JavaType.constructParametricType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametricType(ArrayList.class, String.class);
        TypeBindings bindings = TypeBindings.create(clz, keyType, valueType);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("Test");
        Assert.assertEquals(3, bindingsWithUnbound.size());
        Assert.assertEquals("java.lang.String", bindingsWithUnbound.getBoundName(0));
        Assert.assertEquals(keyType, bindingsWithUnbound.getBoundType(0));
        Assert.assertEquals("java.util.List", bindingsWithUnbound.getBoundName(1));
        Assert.assertEquals(valueType, bindingsWithUnbound.getBoundType(1));
        Assert.assertEquals("Test", bindingsWithUnbound.getBoundName(2));
        Assert.assertNull(bindingsWithUnbound.findBoundType("Test"));
    }
}