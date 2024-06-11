import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

import java.util.Arrays;

public class SimpleTypeTest {

    // Basic test cases for constructors
    @org.junit.Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new JavaType[]{}, simpleType.getSuperInterfaces());
    }

    @org.junit.Test
    public void testSimpleTypeConstructorWithClassBindings() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        SimpleType simpleType = new SimpleType(cls, bindings);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new JavaType[]{}, simpleType.getSuperInterfaces());
    }

    @org.junit.Test
    public void testSimpleTypeConstructorWithClassSuperClassSuperInterfaces() {
        Class<?> cls = String.class;
        JavaType superClass = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInterfaces, simpleType.getSuperInterfaces());
    }

    @org.junit.Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType originalSimpleType = new SimpleType(cls, bindings, superClass, superInterfaces);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(originalSimpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertEquals(originalSimpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
    }

    // Test case for the static construct method
    @org.junit.Test
    public void testStaticConstructMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new JavaType[]{}, simpleType.getSuperInterfaces());
    }

    // Test case for the _narrow method
    @org.junit.Test
    public void testNarrowMethod() {
        Class<?> cls = String.class;
        Class<?> subclass = Integer.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType narrowedType = simpleType._narrow(subclass);
        assertNotNull(narrowedType);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        assertEquals(simpleType.getSuperClass(), narrowedType.getSuperClass());
        assertEquals(simpleType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
    }

    // Edge cases and exception handling test cases

    // Test for null input to the constructor
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullInputToConstructor() {
        new SimpleType(null);
    }

    // Test for non-Class input to the constructor
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNonClassInputToConstructor() {
        new SimpleType(new Object() {});
    }

    // Test for invalid super-type in the constructor
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidSuperTypeInConstructor() {
        Class<?> cls = String.class;
        JavaType superClass = JavaType.constructCollectionType(ArrayList.class, SimpleType.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0]);
    }

    // Test for invalid super-interface in the constructor
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidSuperInterfaceInConstructor() {
        Class<?> cls = String.class;
        JavaType superInterface = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, new JavaType[]{superInterface});
    }

    // Test for invalid binding in the constructor
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidBindingInConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, new TypeBindings() {{ addBinding(null, null, null); }}, null, null);
    }

    // Test for calling narrow method with invalid subclass
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        Class<?> cls = String.class;
        Class<?> subclass = Object.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType._narrow(subclass);
    }
}