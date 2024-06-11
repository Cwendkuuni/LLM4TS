import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNotNull(option);
    }
}