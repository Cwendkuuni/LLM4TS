import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType refType = ReferenceType.construct(intClass, TypeBindings.emptyBindings(), null, null, new JavaTypeReference(intType));
        ReferenceType referenceType = (ReferenceType) refType;
        assertNotNull(referenceType);
        assertEquals(intClass, referenceType.getRawClass());
        assertEquals(intType, referenceType.getContentType());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType refType = JavaType.constructParametrizedType(ReferenceType.class, intClass);
        JavaType upgradedType = ReferenceType.upgradeFrom(intType, refType.getTypeParameters().get(0));
        ReferenceType referenceType = (ReferenceType) upgradedType;
        assertNotNull(referenceType);
        assertEquals(intClass, referenceType.getRawClass());
        assertEquals(intType, referenceType.getContentType());
        assertEquals(referenceType, referenceType.getAnchorType());
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testWithContentType() {
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType = (ReferenceType) ReferenceType.construct(intClass, TypeBindings.emptyBindings(), null, null, intType);
        JavaType upgradedType = refType.withContentType(stringType);
        ReferenceType newReferenceType = (ReferenceType) upgradedType;
        assertNotNull(newReferenceType);
        assertEquals(intClass, newReferenceType.getRawClass());
        assertEquals(stringType, newReferenceType.getContentType());
        assertEquals(refType, newReferenceType.getAnchorType());
        assertTrue(newReferenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefType() {
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType refType = JavaType.constructParametrizedType(ReferenceType.class, intClass);
        ReferenceType.upgradeFrom(intType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType refType = JavaType.constructParametrizedType(ReferenceType.class, intClass);
        ReferenceType.upgradeFrom(intType, new JavaTypeReference(String.class));
    }

    @Test
    public void testToString() {
        Class<Integer> intClass = Integer.class;
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType refType = JavaType.constructParametrizedType(ReferenceType.class, intClass);
        ReferenceType referenceType = (ReferenceType) ReferenceType.upgradeFrom(intType, refType.getTypeParameters().get(0));
        String result = referenceType.toString();
        String expected = "[reference type, class [I<java.lang.Integer>, class [Ljava.lang.Object;>]";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<Integer> intClass = Integer.class;
        JavaType intType1 = TypeFactory.defaultInstance().constructType(intClass);
        JavaType intType2 = TypeFactory.defaultInstance().constructType(intClass);
        JavaType refType1 = JavaType.constructParametrizedType(ReferenceType.class, intClass);
        JavaType refType2 = JavaType.constructParametrizedType(ReferenceType.class, intClass);
        ReferenceType referenceType1 = (ReferenceType) ReferenceType.upgradeFrom(intType1, refType1.getTypeParameters().get(0));
        ReferenceType referenceType2 = (ReferenceType) ReferenceType.upgradeFrom(intType2, refType2.getTypeParameters().get(0));
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType1.equals(ReferenceType.upgradeFrom(intType1, refType1.getTypeParameters().get(0))));
        assertFalse(referenceType1.equals("not a valid JavaType"));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(JavaType.constructType(Boolean.class)));
    }
}