import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.*;

import java.util.Collection;
import java.util.Map;

public class SimpleTypeTest {

    // Test case for the constructor that takes a Class argument
    @Test
    public void testConstructorWithClassArgument() {
        SimpleType simpleType = new SimpleType(Map.class);

        Assert.assertEquals(Map.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals(0, simpleType.containedTypeCount());
    }

    // Test case for the constructor that takes a Class argument and other parameters
    @Test
    public void testConstructorWithClassAndOtherArguments() {
        SimpleType simpleType = new SimpleType(Map.class, null, null, null);

        Assert.assertEquals(Map.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals(0, simpleType.containedTypeCount());
    }

    // Test case for the constructor that takes a TypeBase argument
    @Test
    public void testConstructorWithTypeBaseArgument() {
        SimpleType baseType = new SimpleType(Map.class);
        SimpleType simpleType = new SimpleType(baseType);

        Assert.assertEquals(Map.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals(0, simpleType.containedTypeCount());
    }

    // Test case for the constructUnsafe method
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Map.class);

        Assert.assertEquals(Map.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals(0, simpleType.containedTypeCount());
    }

    // Test case for the construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    // Test case for the construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    // Test case for the construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(String[].class);
    }

    // Test case for the _narrow method
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType narrowedType = simpleType.narrow(String.class);

        Assert.assertEquals(String.class, narrowedType.getRawClass());
        Assert.assertFalse(narrowedType.isContainerType());
        Assert.assertEquals(0, narrowedType.containedTypeCount());
    }

    // Test case for the withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType withTypeHandler = simpleType.withTypeHandler("typeHandler");

        Assert.assertEquals(Object.class, withTypeHandler.getRawClass());
        Assert.assertEquals("typeHandler", withTypeHandler.getTypeHandler());
    }

    // Test case for the withValueHandler method
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType withValueHandler = simpleType.withValueHandler("valueHandler");

        Assert.assertEquals(Object.class, withValueHandler.getRawClass());
        Assert.assertEquals("valueHandler", withValueHandler.getValueHandler());
    }

    // Test case for the withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();

        Assert.assertTrue(withStaticTyping.isStaticTyping());
    }

    // Test case for the refine method
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(Map.class, null, null, null);

        Assert.assertNull(refinedType);
    }

    // Test case for the getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Map.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);

        Assert.assertNotNull(erasedSignature);
    }

    // Test case for the getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Map.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);

        Assert.assertNotNull(genericSignature);
    }

    // Test case for the toString method
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Map.class);
        String toString = simpleType.toString();

        Assert.assertNotNull(toString);
    }

    // Test case for the equals method
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Map.class);
        SimpleType simpleType2 = new SimpleType(Map.class);

        Assert.assertTrue(simpleType1.equals(simpleType2));
    }
}