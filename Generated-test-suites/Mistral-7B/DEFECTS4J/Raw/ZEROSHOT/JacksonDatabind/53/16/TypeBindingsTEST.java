import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        Assert.assertEquals(0, empty.size());
        Assert.assertNull(empty.findBoundType("test"));
        Assert.assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateSingleTypeArgument() {
        Class<List<String>> listOfStringClass = List.of(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStringClass, null);
        Assert.assertEquals(1, bindings.size());
        Assert.assertEquals("E", bindings.getBoundName(0));
        Assert.assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        Assert.assertNull(bindings.findBoundType("test"));
        Assert.assertFalse(bindings.hasUnbound("E"));
    }

    @Test
    public void testCreateMultipleTypeArguments() {
        Class<Map<String, List<String>>> mapOfListOfStringClass = Map.of(String.class, List.class, List.class, String.class);
        TypeBindings bindings = TypeBindings.create(mapOfListOfStringClass, null);
        Assert.assertEquals(2, bindings.size());
        Assert.assertEquals("K", bindings.getBoundName(0));
        Assert.assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        Assert.assertEquals("V", bindings.getBoundName(1));
        Assert.assertEquals(List.class, bindings.getBoundType(1).getRawClass());
        Assert.assertNull(bindings.findBoundType("test"));
        Assert.assertFalse(bindings.hasUnbound("K"));
        Assert.assertFalse(bindings.hasUnbound("V"));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<List<String>, List<List<String>>>> mapOfListOfListOfStringClass = Map.of(List.class, List.class, List.class, List.class);
        TypeBindings bindings = TypeBindings.create(mapOfListOfListOfStringClass, null);
        Assert.assertEquals(2, bindings.size());

        Assert.assertEquals("V", bindings.getBoundName(0));
        Assert.assertEquals(List.class, bindings.getBoundType(0).getRawClass());

        Assert.assertEquals("V", bindings.getBoundName(1));
        Assert.assertEquals(List.class, bindings.getBoundType(1).getRawClass());

        Assert.assertEquals(List.class, bindings.findBoundType("V").getRawClass());
        Assert.assertEquals(List.class, bindings.findBoundType("V").getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidBindings() {
        Class<Map<List<String>, List<List<String>>>> mapOfListOfListOfStringClass = Map.of(List.class, List.class, List.class, List.class);
        List<TypeVariable<?>> typeParameters = Arrays.asList(mapOfListOfListOfStringClass.getTypeParameters());
        TypeBindings bindings = TypeBindings.create(mapOfListOfListOfStringClass, null);

        Assert.assertEquals(4, bindings.size());

        TypeVariable<?>[] params = TypeBindings.TypeParamStash.paramsFor2(mapOfListOfListOfStringClass);
        TypeBindings invalidBindings = TypeBindings.create(mapOfListOfListOfStringClass, params);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            invalidBindings.findBoundType("test");
        });
    }
}