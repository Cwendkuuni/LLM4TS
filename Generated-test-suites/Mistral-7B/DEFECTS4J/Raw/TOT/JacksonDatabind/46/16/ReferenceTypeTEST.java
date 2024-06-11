import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);

        // Test basic properties
        Assert.assertEquals(stringClass, referenceType.getRawClass());
        Assert.assertNotNull(referenceType.getReferencedType());
        Assert.assertFalse(referenceType.isStatic());

        // Test factory methods
        ReferenceType referenceTypeWithTypeHandler = referenceType.withTypeHandler(null);
        Assert.assertNotSame(referenceType, referenceTypeWithTypeHandler);

        ReferenceType referenceTypeWithContentTypeHandler = referenceTypeWithTypeHandler.withContentTypeHandler(null);
        Assert.assertNotSame(referenceTypeWithTypeHandler, referenceTypeWithContentTypeHandler);

        ReferenceType referenceTypeWithValueHandler = referenceTypeWithContentTypeHandler.withValueHandler(null);
        Assert.assertNotSame(referenceTypeWithContentTypeHandler, referenceTypeWithValueHandler);

        ReferenceType referenceTypeWithContentValueHandler = referenceTypeWithValueHandler.withContentValueHandler(null);
        Assert.assertNotSame(referenceTypeWithValueHandler, referenceTypeWithContentValueHandler);

        ReferenceType referenceTypeWithStaticTyping = referenceTypeWithContentValueHandler.withStaticTyping();
        Assert.assertNotSame(referenceTypeWithContentValueHandler, referenceTypeWithStaticTyping);

        // Test methods that return the same instance
        ReferenceType referenceTypeWithSameTypeHandler = referenceTypeWithTypeHandler.withTypeHandler(referenceType._typeHandler);
        Assert.assertSame(referenceTypeWithTypeHandler, referenceTypeWithSameTypeHandler);

        ReferenceType referenceTypeWithSameContentTypeHandler = referenceTypeWithContentTypeHandler.withContentTypeHandler(referenceType._referencedType.getTypeHandler());
        Assert.assertSame(referenceTypeWithContentTypeHandler, referenceTypeWithSameContentTypeHandler);

        ReferenceType referenceTypeWithSameValueHandler = referenceTypeWithValueHandler.withValueHandler(referenceType._valueHandler);
        Assert.assertSame(referenceTypeWithValueHandler, referenceTypeWithSameValueHandler);

        ReferenceType referenceTypeWithSameContentValueHandler = referenceTypeWithContentValueHandler.withContentValueHandler(referenceType._referencedType.getValueHandler());
        Assert.assertSame(referenceTypeWithContentValueHandler, referenceTypeWithSameContentValueHandler);

        // Test methods that create a new instance
        ReferenceType referenceTypeWithDifferentTypeHandler = referenceType.withTypeHandler(new Object());
        Assert.assertNotSame(referenceType, referenceTypeWithDifferentTypeHandler);

        ReferenceType referenceTypeWithDifferentContentTypeHandler = referenceTypeWithTypeHandler.withContentTypeHandler(new Object());
        Assert.assertNotSame(referenceTypeWithTypeHandler, referenceTypeWithDifferentContentTypeHandler);

        ReferenceType referenceTypeWithDifferentValueHandler = referenceTypeWithContentTypeHandler.withValueHandler(new Object());
        Assert.assertNotSame(referenceTypeWithContentTypeHandler, referenceTypeWithDifferentValueHandler);

        ReferenceType referenceTypeWithDifferentContentValueHandler = referenceTypeWithValueHandler.withContentValueHandler(new Object());
        Assert.assertNotSame(referenceTypeWithValueHandler, referenceTypeWithDifferentContentValueHandler);

        ReferenceType referenceTypeWithDifferentStaticTyping = referenceTypeWithContentValueHandler.withStaticTyping();
        Assert.assertNotSame(referenceTypeWithContentValueHandler, referenceTypeWithDifferentStaticTyping);

        // Test immutability
        ReferenceType immutableReferenceType = referenceTypeWithDifferentStaticTyping.withTypeHandler(referenceType._typeHandler);
        ReferenceType immutableReferenceType2 = referenceTypeWithDifferentStaticTyping.withContentTypeHandler(referenceType._referencedType.getTypeHandler());
        ReferenceType immutableReferenceType3 = referenceTypeWithDifferentStaticTyping.withValueHandler(referenceType._valueHandler);
        ReferenceType immutableReferenceType4 = referenceTypeWithDifferentStaticTyping.withContentValueHandler(referenceType._referencedType.getValueHandler());
        Assert.assertNotSame(immutableReferenceType, immutableReferenceType2);
        Assert.assertNotSame(immutableReferenceType2, immutableReferenceType3);
        Assert.assertNotSame(immutableReferenceType3, immutableReferenceType4);
        Assert.assertSame(immutableReferenceType, immutableReferenceType);

        // Test equals and hashCode
        ReferenceType equalReferenceType = new ReferenceType(stringClass, javaType, null, null);
        Assert.assertEquals(referenceType, equalReferenceType);
        Assert.assertEquals(referenceType.hashCode(), equalReferenceType.hashCode());

        ReferenceType notEqualReferenceType = new ReferenceType(String[].class, javaType, null, null);
        Assert.assertNotEquals(referenceType, notEqualReferenceType);
        Assert.assertNotEquals(referenceType.hashCode(), notEqualReferenceType.hashCode());

        // Test narrow method
        ReferenceType narrowedReferenceType = referenceType.narrow(String[].class);
        Assert.assertNotNull(narrowedReferenceType);
        Assert.assertNotSame(referenceType, narrowedReferenceType);
        Assert.assertEquals(String[].class, narrowedReferenceType.getRawClass());
        Assert.assertEquals(javaType, narrowedReferenceType.getReferencedType());

        // Test getReferencedType and isReferenceType methods
        Assert.assertTrue(referenceType.isReferenceType());
        Assert.assertEquals(javaType, referenceType.getReferencedType());

        // Test containedTypeCount, containedType, containedTypeName, getParameterSource, getErasedSignature and getGenericSignature methods
        Assert.assertEquals(1, referenceType.containedTypeCount());
        Assert.assertEquals(javaType, referenceType.containedType(0));
        Assert.assertNull(referenceType.containedTypeName(0));
        Assert.assertEquals(stringClass, referenceType.getParameterSource());
        Assert.assertNotNull(referenceType.getErasedSignature(null));
        Assert.assertNotNull(referenceType.getGenericSignature(null));
    }
}