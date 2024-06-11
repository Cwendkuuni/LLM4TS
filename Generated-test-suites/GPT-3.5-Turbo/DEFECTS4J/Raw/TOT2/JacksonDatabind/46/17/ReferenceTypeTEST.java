import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, new SimpleType(), null, null);
        assertEquals(String.class, referenceType.getRawClass());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(true, newReferenceType.isStaticTyping());
    }

    @Test
    public void testNarrow() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(), null, null, false);
        ReferenceType narrowedType = (ReferenceType)referenceType._narrow(Object.class);
        assertNotEquals(referenceType, narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

}