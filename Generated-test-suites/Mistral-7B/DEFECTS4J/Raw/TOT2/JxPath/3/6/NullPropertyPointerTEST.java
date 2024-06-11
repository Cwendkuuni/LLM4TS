import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getName().getLocalPart());

        NullPropertyPointer nullPropertyPointerWithName = new NullPropertyPointer(null);
        nullPropertyPointerWithName.setPropertyName("testProperty");
        assertEquals("testProperty", nullPropertyPointerWithName.getName().getLocalPart());
    }
}