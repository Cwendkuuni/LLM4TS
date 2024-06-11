import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

@RunWith(JUnit4.class)
public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertFalse(simpleType.isStatic());
        assertEquals(simpleType.toString(), "[simple type, class " + simpleClass.getName() + "]");

        SimpleType simpleTypeWithBindings = new SimpleType(simpleClass, TypeBindings.emptyBindings());
        assertEquals(simpleClass, simpleTypeWithBindings.getRawClass());
        assertNull(simpleTypeWithBindings.getSuperClass());
        assertNull(simpleTypeWithBindings.getSuperInterfaces());
        assertFalse(simpleTypeWithBindings.isContainerType());
        assertFalse(simpleTypeWithBindings.isStatic());
        assertEquals(simpleTypeWithBindings.toString(), "[simple type, class " + simpleClass.getName() + "]");

        SimpleType simpleTypeWithSuperClass = new SimpleType(simpleClass, null, new JavaType() {});
        assertEquals(simpleClass, simpleTypeWithSuperClass.getRawClass());
        assertNotNull(simpleTypeWithSuperClass.getSuperClass());
        assertNull(simpleTypeWithSuperClass.getSuperInterfaces());
        assertFalse(simpleTypeWithSuperClass.isContainerType());
        assertFalse(simpleTypeWithSuperClass.isStatic());
        assertEquals(simpleTypeWithSuperClass.toString(), "[simple type, class " + simpleClass.getName() + ", super-class=class ?, interfaces=[]]");

        SimpleType simpleTypeWithSuperClassAndInterfaces = new SimpleType(simpleClass, null, new JavaType() {}, new JavaType[] {new JavaType() {}});
        assertEquals(simpleClass, simpleTypeWithSuperClassAndInterfaces.getRawClass());
        assertNotNull(simpleTypeWithSuperClassAndInterfaces.getSuperClass());
        assertArrayEquals(new JavaType[] {new JavaType() {}}, simpleTypeWithSuperClassAndInterfaces.getSuperInterfaces());
        assertFalse(simpleTypeWithSuperClassAndInterfaces.isContainerType());
        assertFalse(simpleTypeWithSuperClassAndInterfaces.isStatic());
        assertEquals(simpleTypeWithSuperClassAndInterfaces.toString(), "[simple type, class " + simpleClass.getName() + ", super-class=class ?, interfaces=[class ?]]");

        SimpleType simpleTypeWithValueHandler = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null, new Object() {});
        assertEquals(simpleClass, simpleTypeWithValueHandler.getRawClass());
        assertNull(simpleTypeWithValueHandler.getSuperClass());
        assertNull(simpleTypeWithValueHandler.getSuperInterfaces());
        assertFalse(simpleTypeWithValueHandler.isContainerType());
        assertTrue(simpleTypeWithValueHandler.isStatic());
        assertEquals(simpleTypeWithValueHandler.toString(), "[simple type, class " + simpleClass.getName() + ", handlers={valueHandler=class ?, typeHandler=null}]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalClassConstruction() {
        new SimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCollectionConstruction() {
        new SimpleType(List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArrayConstruction() {
        new SimpleType(int[].class);
    }

    @Test
    public void testNarrowing() {
        Class<?> parentClass = Object.class;
        SimpleType parentType = new SimpleType(parentClass);
        SimpleType childType = new SimpleType(String.class);
        SimpleType narrowedType = childType.narrow(parentClass);
        assertEquals(childType, narrowedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testNarrowingWrongClass() {
        Class<?> parentClass = Object.class;
        SimpleType childType = new SimpleType(int.class);
        SimpleType narrowedType = childType.narrow(parentClass);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        StringBuilder signature = new StringBuilder();
        simpleType.getErasedSignature(signature);
        assertEquals("java.lang.String", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        StringBuilder signature = new StringBuilder();
        simpleType.getGenericSignature(signature);
        assertEquals("<T extends java.lang.String>", signature.toString());
    }
}