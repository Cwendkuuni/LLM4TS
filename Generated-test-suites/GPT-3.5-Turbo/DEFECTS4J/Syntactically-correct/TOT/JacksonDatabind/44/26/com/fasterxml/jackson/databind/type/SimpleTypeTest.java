
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(SuperClass.class);
        JavaType[] superInterfaces = new JavaType[]{new SimpleType(Interface1.class), new SimpleType(Interface2.class)};
        SimpleType simpleType = new SimpleType(TestClass.class, bindings, superClass, superInterfaces);
        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertEquals(bindings, simpleType.getBindings());
        Assert.assertEquals(superClass, simpleType.getSuperClass());
        Assert.assertArrayEquals(superInterfaces, simpleType.getInterfaces());
    }

    @Test
    public void testConstructor3() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(TestClass.class, TypeBindings.emptyBindings(), null,
                null, valueHandler, typeHandler, false);
        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertEquals(valueHandler, simpleType.getValueHandler());
        Assert.assertEquals(typeHandler, simpleType.getTypeHandler());
    }

    @Test
    public void testConstructor4() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(TestClass.class, TypeBindings.emptyBindings(), null,
                null, valueHandler, typeHandler, false);
        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
        Assert.assertEquals(valueHandler, simpleType.getValueHandler());
        Assert.assertEquals(typeHandler, simpleType.getTypeHandler());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(TestClass.class);
        Assert.assertEquals(TestClass.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(TestClassMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(TestClassCollection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(TestClassArray.class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        JavaType narrowedType = simpleType._narrow(ChildClass.class);
        Assert.assertEquals(ChildClass.class, narrowedType.getRawClass());
        Assert.assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        Assert.assertEquals(simpleType.getSuperClass(), narrowedType.getSuperClass());
        Assert.assertArrayEquals(simpleType.getInterfaces(), narrowedType.getInterfaces());
        Assert.assertEquals(simpleType.getValueHandler(), narrowedType.getValueHandler());
        Assert.assertEquals(simpleType.getTypeHandler(), narrowedType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        simpleType._narrow(SuperClass.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        try {
            simpleType.withContentType(new SimpleType(TestClass.class));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(TestClass.class);
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        Assert.assertEquals(TestClass.class, typeWithHandler.getRawClass());
        Assert.assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithTypeHandlerThrowsException() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(TestClass.class);
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        try {
            typeWithHandler.withTypeHandler(new Object());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(TestClass.class);
        SimpleType typeWithValueHandler = simpleType.withValueHandler(handler);
        Assert.assertEquals(TestClass.class, typeWithValueHandler.getRawClass());
        Assert.assertEquals(handler, typeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerThrowsException() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(TestClass.class);
        SimpleType typeWithValueHandler = simpleType.withValueHandler(handler);
        try {
            typeWithValueHandler.withContentValueHandler(new Object());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        SimpleType typeWithStaticTyping = simpleType.withStaticTyping();
        Assert.assertEquals(TestClass.class, typeWithStaticTyping.getRawClass());
        Assert.assertTrue(typeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        JavaType refinedType = simpleType.refine(TestClass.class, TypeBindings.emptyBindings(),
                new SimpleType(ChildClass.class), new JavaType[]{new SimpleType(Interface1.class)});
        Assert.assertNull(refinedType);
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
        Assert.assertEquals("L"+ TestClass.class.getName().replace('.', '/') +";", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        Assert.assertEquals("L"+ TestClass.class.getName().replace('.', '/') +";", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(TestClass.class);
        String expected = "[simple type, class " + TestClass.class.getCanonicalName() + "]";
        Assert.assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(TestClass.class);
        SimpleType type2 = new SimpleType(TestClass.class);
        Assert.assertTrue(type1.equals(type2));
    }

    public static class TestClass {
    }

    public static class ChildClass extends TestClass {
    }

    public static class SuperClass {
    }

    public static interface Interface1 {
    }

    public static interface Interface2 {
    }

    public static class TestClassMap extends HashMap<String, String> {
    }

    public static class TestClassCollection extends ArrayList<String> {
    }

    public static class TestClassArray extends String[] {
    }
}
