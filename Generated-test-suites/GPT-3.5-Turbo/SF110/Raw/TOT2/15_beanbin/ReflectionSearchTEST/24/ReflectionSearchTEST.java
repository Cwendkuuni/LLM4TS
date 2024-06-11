import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertFalse(reflectionSearch.hasAnnotation("Test"));
    }

    @Test
    public void testHasAnnotationWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertTrue(reflectionSearch.hasAnnotation("java.lang.*"));
    }

    @Test(expected = NullPointerException.class)
    public void testHasAnnotationNull() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(null);
        reflectionSearch.hasAnnotation("Test");
    }
}