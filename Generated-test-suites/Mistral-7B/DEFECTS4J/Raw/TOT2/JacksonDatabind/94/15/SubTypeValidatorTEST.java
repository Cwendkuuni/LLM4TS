import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SubTypeValidatorTest {
    @Test
    public void testDefaultNoDeserClassNames() {
        Set<String> classNames = SubTypeValidator.getDefaultNoDeserClassNames();
        assertEquals(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES, classNames);
    }
}