import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testConstructorWithoutBindings() {
        SimpleType simpleType = new SimpleType(TestClass.class);

        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertNull(simpleType.getSuperClass());
        Assert.assertEquals(0, simpleType.getInterfaces().size());
    }

    @Test
    public void testConstructorWithBindings() {
        TypeBindings bindings = new TypeBindings();
        bindings.addType(TestClass.class);
        SimpleType simpleType = new SimpleType(TestClass.class, bindings, null, null);

        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertNull(simpleType.getSuperClass());
        Assert.assertEquals(0, simpleType.getInterfaces().size());
        Assert.assertEquals(1, simpleType.getBindings().size());
        Assert.assertTrue(simpleType.getBindings().containsType(TestClass.class));
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(TestClass.class);

        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertNull(simpleType.getSuperClass());
        Assert.assertEquals(0, simpleType.getInterfaces().size());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            SimpleType.construct(Collection.class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        try {
            SimpleType.construct(TestClass[].class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }

        SimpleType simpleType = SimpleType.construct(TestClass.class);

        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertNull(simpleType.getSuperClass());
        Assert.assertEquals(0, simpleType.getInterfaces().size());
    }

    @Test
    public void testConstructorWithSuperClassAndSuperInterfaces() {
        SimpleType simpleType = new SimpleType(TestClass.class, null, TestSuperClass.class, new JavaType[] { new SimpleType(TestInterface.class) });

        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertEquals(TestSuperClass.class, simpleType.getSuperClass().getRawClass());
        Assert.assertEquals(1, simpleType.getInterfaces().size());
        Assert.assertEquals(TestInterface.class, simpleType.getInterfaces().get(0).getRawClass());
    }

    @Test
    public void testConstructorWithAdditionalParameters() {
        Object dummyObject = new Object();
        SimpleType simpleType = new SimpleType(TestClass.class, null, TestSuperClass.class, new JavaType[] { new SimpleType(TestInterface.class) }, dummyObject, dummyObject, true);

        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertEquals(TestSuperClass.class, simpleType.getSuperClass().getRawClass());
        Assert.assertEquals(1, simpleType.getInterfaces().size());
        Assert.assertEquals(TestInterface.class, simpleType.getInterfaces().get(0).getRawClass());
        Assert.assertEquals(dummyObject, simpleType.getValueHandler());
        Assert.assertEquals(dummyObject, simpleType.getTypeHandler());
        Assert.assertTrue(simpleType.isStaticTyping());
        Assert.assertEquals(0, simpleType.getBindings().size());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        JavaType narrowedType = simpleType.narrow(TestClass.class);

        Assert.assertSame(simpleType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(TestClass.class);

        try {
            simpleType.withContentType(new SimpleType(TestClass.class));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(TestClass.class);

        try {
            simpleType.withContentValueHandler(new Object());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testWithGenericSignature() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);

        Assert.assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleType<com/fasterxml/jackson/databind/type/SimpleTypeTest$TestClass;>;", sb.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(TestClass.class);

        Assert.assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);

        Assert.assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleType;", sb.toString());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        JavaType refinedType = simpleType.refine(TestClass.class, new TypeBindings(), null, new JavaType[] {});

        Assert.assertNull(refinedType);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(TestClass.class);

        Assert.assertEquals("[simple type, class com.fasterxml.jackson.databind.type.SimpleTypeTest$TestClass]", simpleType.toString().replaceAll("[0-9]", ""));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(TestClass.class);
        SimpleType simpleType2 = new SimpleType(TestClass.class);

        Assert.assertEquals(simpleType1, simpleType1);
        Assert.assertEquals(simpleType1, simpleType2);
        Assert.assertEquals(simpleType2, simpleType1);
        Assert.assertEquals(simpleType1.hashCode(), simpleType2.hashCode());

        SimpleType simpleType3 = new SimpleType(TestSuperClass.class);
        SimpleType simpleType4 = new SimpleType(TestSuperClass.class);

        Assert.assertEquals(simpleType3, simpleType3);
        Assert.assertEquals(simpleType3, simpleType4);
        Assert.assertEquals(simpleType4, simpleType3);
        Assert.assertEquals(simpleType3.hashCode(), simpleType4.hashCode());
    }

    private static class TestClass {

    }

    private static class TestSuperClass {

    }

    private static interface TestInterface {

    }
}