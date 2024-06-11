import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Test class for {@link StdValueInstantiator}.
 */
public class StdValueInstantiatorTest {

    /**
     * Test default constructor with no arguments.
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, null);
        Assert.assertEquals("UNKNOWN TYPE", instantiatable.getValueTypeDesc());
        Assert.assertEquals(Object.class, instantiatable.getValueClass());
        Assert.assertFalse(instantiatable.canCreateUsingDefault());
        Assert.assertNull(instantiatable.getDelegateType(null));
        Assert.assertNull(instantiatable.getDelegateCreator());
        Assert.assertNull(instantiatable.getDelegateArguments());
        Assert.assertFalse(instantiatable.canCreateUsingDelegate());
        Assert.assertNull(instantiatable.getArrayDelegateType(null));
        Assert.assertNull(instantiatable.getArrayDelegateCreator());
        Assert.assertNull(instantiatable.getArrayDelegateArguments());
        Assert.assertFalse(instantiatable.canCreateFromObjectWith());
        Assert.assertNull(instantiatable.getFromObjectArguments(null));

        Object defaultInstance = instantiatable.createUsingDefault(null);
        Assert.assertNotNull(defaultInstance);

        // Test error scenario: if no default constructor is set, the default constructor should be used
        try {
            instantiatable = new StdValueInstantiator(null, null) {
                @Override
                public boolean canCreateUsingDefault() {
                    return false;
                }
            };
            instantiatable.createUsingDefault(null);
            Assert.fail("Expected InstantiationException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof com.fasterxml.jackson.core.JsonProcessingException);
        }
    }

    /**
     * Test constructor with a single argument.
     */
    @Test
    public void testSingleArgumentConstructor() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, String.class);
        Assert.assertEquals("java.lang.String", instantiatable.getValueTypeDesc());
        Assert.assertEquals(String.class, instantiatable.getValueClass());
        Assert.assertFalse(instantiatable.canCreateUsingDefault());
        Assert.assertNull(instantiatable.getDelegateType(null));
        Assert.assertNull(instantiatable.getDelegateCreator());
        Assert.assertNull(instantiatable.getDelegateArguments());
        Assert.assertFalse(instantiatable.canCreateUsingDelegate());
        Assert.assertNull(instantiatable.getArrayDelegateType(null));
        Assert.assertNull(instantiatable.getArrayDelegateCreator());
        Assert.assertNull(instantiatable.getArrayDelegateArguments());
        Assert.assertFalse(instantiatable.canCreateFromObjectWith());
        Assert.assertNull(instantiatable.getFromObjectArguments(null));

        Object instance = instantiatable.createUsingDefault(new ObjectMapper().getFactory());
        Assert.assertNotNull(instance);

        Assert.assertTrue(instantiatable.canCreateFromString());
        String stringInstance = instantiatable.createFromString(new ObjectMapper().getFactory(), "Hello World");
        Assert.assertEquals("Hello World", stringInstance);
    }

    /**
     * Test constructor with multiple arguments.
     */
    @Test
    public void testMultipleArgumentsConstructor() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, Foo.class);
        Assert.assertEquals("com.example.Foo", instantiatable.getValueTypeDesc());
        Assert.assertEquals(Foo.class, instantiatable.getValueClass());
        Assert.assertFalse(instantiatable.canCreateUsingDefault());
        Assert.assertNull(instantiatable.getDelegateType(null));
        Assert.assertNull(instantiatable.getDelegateCreator());
        Assert.assertNull(instantiatable.getDelegateArguments());
        Assert.assertFalse(instantiatable.canCreateUsingDelegate());
        Assert.assertNull(instantiatable.getArrayDelegateType(null));
        Assert.assertNull(instantiatable.getArrayDelegateCreator());
        Assert.assertNull(instantiatable.getArrayDelegateArguments());
        Assert.assertFalse(instantiatable.canCreateFromObjectWith());
        Assert.assertNull(instantiatable.getFromObjectArguments(null));

        Object[] constructorArguments = new Object[]{new Integer(1), new Double(3.14), new Boolean(true)};
        instantiatable.configureFromObjectSettings(null, null, null, constructorArguments, null, null);

        Object instance = instantiatable.createFromObjectWith(new ObjectMapper().getFactory(), constructorArguments);
        Assert.assertNotNull(instance);
        Assert.assertEquals(1, ((Foo) instance).getIntValue());
        Assert.assertEquals(3.14, ((Foo) instance).getDoubleValue(), 0.0);
        Assert.assertTrue(((Foo) instance).isBooleanValue());

        try {
            instantiatable.createFromObjectWith(new ObjectMapper().getFactory(), new Object[]{1, 2.0, true, "Hello"});
            Assert.fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    /**
     * Test delegate constructor.
     */
    @Test
    public void testDelegateConstructor() throws IOException {
        FooDelegate delegate = new FooDelegate();
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, Foo.class);
        instantiatable.configureUsingDelegate(null, null, FooDelegate.class, null);

        Object instance = instantiatable.createUsingDelegate(new ObjectMapper().getFactory(), delegate);
        Assert.assertNotNull(instance);
        Assert.assertEquals(delegate.getIntValue(), ((Foo) instance).getIntValue());
        Assert.assertEquals(delegate.getDoubleValue(), ((Foo) instance).getDoubleValue(), 0.0);
        Assert.assertTrue(((Foo) instance).isBooleanValue());

        try {
            instantiatable.createUsingDelegate(new ObjectMapper().getFactory(), new Object());
            Assert.fail("Expected IllegalStateException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalStateException);
        }
    }

    /**
     * Test array delegate constructor.
     */
    @Test
    public void testArrayDelegateConstructor() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, int[].class);
        instantiatable.configureFromArraySettings(null, JavaClassIntArray.class, null);

        int[] instance = instantiatable.createUsingArrayDelegate(new ObjectMapper().getFactory(), new Object[]{});
        Assert.assertNotNull(instance);
        Assert.assertArrayEquals(new int[0], instance);

        int[] constructorArguments = new int[]{1, 2, 3};
        instantiatable.configureFromArraySettings(null, null, null, null, JavaClassIntArray.class, constructorArguments);

        instance = instantiatable.createUsingArrayDelegate(new ObjectMapper().getFactory(), constructorArguments);
        Assert.assertNotNull(instance);
        Assert.assertArrayEquals(new int[]{1, 2, 3}, instance);
    }

    /**
     * Test string creator.
     */
    @Test
    public void testStringCreator() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, String.class);
        instantiatable.configureFromStringCreator(null);

        Object instance = instantiatable.createFromString(new ObjectMapper().getFactory(), "Hello World");
        Assert.assertNotNull(instance);
        Assert.assertEquals("Hello World", instance);

        try {
            instantiatable.createFromString(new ObjectMapper().getFactory(), null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    /**
     * Test int creator.
     */
    @Test
    public void testIntCreator() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, Integer.class);
        instantiatable.configureFromIntCreator(null);

        Object instance = instantiatable.createFromInt(new ObjectMapper().getFactory(), 123);
        Assert.assertNotNull(instance);
        Assert.assertEquals(123, instance);

        try {
            instantiatable.createFromInt(new ObjectMapper().getFactory(), "123");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    /**
     * Test long creator.
     */
    @Test
    public void testLongCreator() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, Long.class);
        instantiatable.configureFromLongCreator(null);

        Object instance = instantiatable.createFromLong(new ObjectMapper().getFactory(), 123L);
        Assert.assertNotNull(instance);
        Assert.assertEquals(123L, instance);

        try {
            instantiatable.createFromLong(new ObjectMapper().getFactory(), "123");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    /**
     * Test double creator.
     */
    @Test
    public void testDoubleCreator() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, Double.class);
        instantiatable.configureFromDoubleCreator(null);

        Object instance = instantiatable.createFromDouble(new ObjectMapper().getFactory(), 123.45);
        Assert.assertNotNull(instance);
        Assert.assertEquals(123.45, instance, 0.0);

        try {
            instantiatable.createFromDouble(new ObjectMapper().getFactory(), "123.45");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    /**
     * Test boolean creator.
     */
    @Test
    public void testBooleanCreator() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, Boolean.class);
        instantiatable.configureFromBooleanCreator(null);

        Object instance = instantiatable.createFromBoolean(new ObjectMapper().getFactory(), true);
        Assert.assertNotNull(instance);
        Assert.assertEquals(true, instance);

        try {
            instantiatable.createFromBoolean(new ObjectMapper().getFactory(), "true");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    /**
     * Test incomplete parameter creator.
     */
    @Test
    public void testIncompleteParameterCreator() throws IOException {
        StdValueInstantiator instantiatable = new StdValueInstantiator(null, Foo.class);
        instantiatable.configureIncompleteParameter(new AnnotatedParameter(Foo.class.getMethod("setName", String.class), 0));

        Object instance = instantiatable.createUsingIncompleteParameter(new ObjectMapper().getFactory(), "Name");
        Assert.assertNotNull(instance);
        Assert.assertEquals("Name", ((Foo) instance).getName());

        try {
            instantiatable.createUsingIncompleteParameter(new ObjectMapper().getFactory(), null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IOException e) {
            Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
}

class Foo {
    private int intValue;
    private double doubleValue;
    private boolean booleanValue;

    public Foo(int intValue, double doubleValue, boolean booleanValue) {
        this.intValue = intValue;
        this.doubleValue = doubleValue;
        this.booleanValue = booleanValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setName(String name) {
        // This method is used for testing incomplete parameter creator
    }
}

class FooDelegate {
    private int intValue = 1;
    private double doubleValue = 3.14;
    private boolean booleanValue = true;

    public int getIntValue() {
        return intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }
}

class JavaClassIntArray {
    static int[] create(int... values) {
        return values;
    }
}