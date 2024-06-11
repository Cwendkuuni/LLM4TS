import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackConstructor() {
        // Test constructor with default seed
        Namespace[] defaultSeed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        NamespaceStack stack = new NamespaceStack();
        assertArrayEquals(defaultSeed, stack.getScope());
    }
}