import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        Assert.assertEquals(cls, type.getRawClass());
        Assert.assertNull(type.getSuperClass());
        Assert.assertNull(type.getSuperInterfaces());
        Assert.assertNull(type.getValueHandler());
        Assert.assertNull(type.getTypeHandler());
        Assert.assertFalse(type.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType type1 = new SimpleType(cls);
        SimpleType type2 = new SimpleType(type1);
        Assert.assertEquals(type1, type2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeConstruction() {
        Class<?> cls = ArrayList.class;
        new SimpleType(cls);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> superClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType superType = new SimpleType(superClass);
        SimpleType subType = new SimpleType(subClass);
        SimpleType narrowedType = superType._narrow(subClass);
        Assert.assertEquals(subType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        Class<?> superClass = Object.class;
        Class<?> subClass = Integer[].class;
        SimpleType superType = new SimpleType(superClass);
        superType._narrow(subClass);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        Object handler = new Object();
        SimpleType typeWithHandler = type.withTypeHandler(handler);
        Assert.assertNotSame(type, typeWithHandler);
        Assert.assertEquals(type.getRawClass(), typeWithHandler.getRawClass());
        Assert.assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        Object handler = new Object();
        SimpleType typeWithHandler = type.withValueHandler(handler);
        Assert.assertNotSame(type, typeWithHandler);
        Assert.assertEquals(type.getRawClass(), typeWithHandler.getRawClass());
        Assert.assertEquals(handler, typeWithHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        SimpleType staticType = type.withStaticTyping();
        Assert.assertNotSame(type, staticType);
        Assert.assertEquals(type.getRawClass(), staticType.getRawClass());
        Assert.assertTrue(staticType.isStatic());
        Assert.assertNull(staticType.getValueHandler());
        Assert.assertNull(staticType.getTypeHandler());
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<?> cls = String[].class;
        SimpleType type = new SimpleType(cls);
        Assert.assertEquals("[Ljava.lang.String;", type.buildCanonicalName());
    }

    @Test
    public void testIsContainerTypeMethod() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        Assert.assertFalse(type.isContainerType());
    }
}