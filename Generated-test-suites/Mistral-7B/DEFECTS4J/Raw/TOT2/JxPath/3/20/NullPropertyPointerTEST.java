import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName name = nullPropertyPointer.getName();
        assertNotNull(name);
        assertEquals("*", name.getLocalPart());
    }
}