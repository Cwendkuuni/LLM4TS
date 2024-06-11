import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType(null, "Reference", "SourceName", 1, 1);
    }

    @Test
    public void testGetReferencedType() {
        JSType expectedType = null;
        JSType resultType = namedType.getReferencedType();
        assertEquals(expectedType, resultType);
    }

    @Test
    public void testHasReferenceName() {
        boolean expectedResult = true;
        boolean result = namedType.hasReferenceName();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsNamedType() {
        boolean expectedResult = true;
        boolean result = namedType.isNamedType();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsNominalType() {
        boolean expectedResult = true;
        boolean result = namedType.isNominalType();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = null;
        StaticScope<JSType> enclosing = null;
        JSType expectedType = null;
        JSType resultType = namedType.resolveInternal(errorReporter, enclosing);
        assertEquals(expectedType, resultType);
    }

}