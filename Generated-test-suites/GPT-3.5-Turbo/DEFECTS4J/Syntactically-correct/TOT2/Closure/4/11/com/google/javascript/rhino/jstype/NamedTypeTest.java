import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {
    
    private NamedType namedType;
    
    @Before
    public void setUp() {
        namedType = new NamedType(new JSTypeRegistry(), "TestType", "TestSource", 1, 1);
    }
    
    @Test
    public void testGetReferencedType() {
        assertNotNull(namedType.getReferencedType());
    }
    
    @Test
    public void testGetReferenceName() {
        assertEquals("TestType", namedType.getReferenceName());
    }
    
    @Test
    public void testToStringHelper() {
        assertEquals("TestType", namedType.toStringHelper(false));
    }
    
    @Test
    public void testHasReferenceName() {
        assertEquals(true, namedType.hasReferenceName());
    }
    
    @Test
    public void testIsNamedType() {
        assertEquals(true, namedType.isNamedType());
    }
    
    @Test
    public void testIsNominalType() {
        assertEquals(true, namedType.isNominalType());
    }
    
    @Test
    public void testResolveInternal() {
        // To be implemented
    }
}