import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp() {
        namedType = new NamedType(new JSTypeRegistry(), "test", "TestSource", 1, 0);
    }

    @Test
    public void testGetReferencedType() {
        JSType expected = new JSType(new JSTypeRegistry(), "test");
        namedType.setReferencedType(expected);
        JSType actual = namedType.getReferencedType();
        assertEquals(expected, actual);
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();
        namedType.resolveInternal(errorReporter, enclosing);
        JSType actual = namedType.isResolved() ? namedType.getReferencedType() : namedType;
        assertTrue(actual.isNoResolvedType());
    }

    @Test
    public void testSetValidator() {
        Predicate<JSType> validator = type -> type.isNoObjectType();
        boolean actual = namedType.setValidator(validator);
        assertTrue(actual);
    }
}