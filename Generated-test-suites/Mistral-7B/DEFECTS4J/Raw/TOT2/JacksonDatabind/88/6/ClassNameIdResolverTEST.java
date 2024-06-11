import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        String expectedId = resolver.idFromValue(value);
        // Add assertion here for the expectedId
    }
}