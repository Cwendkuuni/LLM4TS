import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructorWithClass() {
        SimpleType type = new SimpleType(Object.class);
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals(0, type.getBindings().size());
        assertNotNull(type.getErasedSignature(new StringBuilder()));
        assertNotNull(type.getGenericSignature(new StringBuilder()));
        assertNotNull(type.toString());
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
        assertFalse(type.equals(new Object()));
    }

    @Test
    public void testContructorWithSuperClass() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType type = new SimpleType(Object.class, TypeBindings.emptyBindings(), superClass, null);
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
        assertEquals(superClass, type.getSuperClass());
        assertFalse(type.isContainerType());
        assertEquals(0, type.getBindings().size());
        assertNotNull(type.getErasedSignature(new StringBuilder()));
        assertNotNull(type.getGenericSignature(new StringBuilder()));
        assertNotNull(type.toString());
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
        assertFalse(type.equals(new Object()));
    }

    @Test
    public void testConstructorWithTypeBindings() {
        TypeBindings bindings = TypeBindings.create(Object.class, new SimpleType[]{});
        SimpleType type = new SimpleType(Object.class, bindings, null, null);
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals(bindings, type.getBindings());
        assertNotNull(type.getErasedSignature(new StringBuilder()));
        assertNotNull(type.getGenericSignature(new StringBuilder()));
        assertNotNull(type.toString());
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
        assertFalse(type.equals(new Object()));
    }

    @Test
    public void testConstructorWithTypeBindingsAndSuperClass() {
        SimpleType superClass = new SimpleType(Object.class);
        TypeBindings bindings = TypeBindings.create(Object.class, new SimpleType[]{});
        SimpleType type = new SimpleType(Object.class, bindings, superClass, null);
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
        assertEquals(superClass, type.getSuperClass());
        assertFalse(type.isContainerType());
        assertEquals(bindings, type.getBindings());
        assertNotNull(type.getErasedSignature(new StringBuilder()));
        assertNotNull(type.getGenericSignature(new StringBuilder()));
        assertNotNull(type.toString());
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
        assertFalse(type.equals(new Object()));
    }

    @Test
    public void testConstructorWithTypeBindingsAndSuperClassAndSuperInterfaces() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInterfaces = new SimpleType[]{new SimpleType(Serializable.class)};
        TypeBindings bindings = TypeBindings.create(Object.class, superInterfaces);
        SimpleType type = new SimpleType(Object.class, bindings, superClass, superInterfaces);
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
        assertEquals(superClass, type.getSuperClass());
        assertFalse(type.isContainerType());
        assertEquals(bindings, type.getBindings());
        assertNotNull(type.getErasedSignature(new StringBuilder()));
        assertNotNull(type.getGenericSignature(new StringBuilder()));
        assertNotNull(type.toString());
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
        assertFalse(type.equals(new Object()));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = new SimpleType(Object.class);
        Object handler = new Object();
        SimpleType typeWithHandler = type.withTypeHandler(handler);
        assertNotNull(typeWithHandler);
        assertEquals(handler, typeWithHandler.getTypeHandler());
        assertEquals(type, typeWithHandler.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(Object.class);
        SimpleType staticType = type.withStaticTyping();
        assertNotNull(staticType);
        assertTrue(staticType.isStatic());
    }
}