import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        QName expectedName = new QName("propertyName");
        QName actualName = pointer.getName();
        assertEquals(expectedName, actualName);
    }
}