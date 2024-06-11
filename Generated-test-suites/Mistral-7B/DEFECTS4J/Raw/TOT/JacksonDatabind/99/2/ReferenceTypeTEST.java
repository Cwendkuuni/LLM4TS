import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getContentType());
        assertEquals(int[].class, referenceType.getRawClass());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(TypeFactory.defaultInstance().constructType(List.class), baseType, referencedType);
        JavaType newContentType = referenceType.withContentType(TypeFactory.defaultInstance().constructType(ArrayList.class));
        assertEquals(ArrayList.class, newContentType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object() {};
        ReferenceType referenceType = new ReferenceType(TypeFactory.defaultInstance().constructType(List.class), baseType, referencedType);
        JavaType newType = referenceType.withTypeHandler(handler);
        assertNotNull(newType);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullReferencedType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }
}