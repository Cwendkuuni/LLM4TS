import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SparseInstanceTest {

    private SparseInstance sparseInstance;

    @Test
    public void testSetNoAttributes() {
        sparseInstance = new SparseInstance();
        sparseInstance.setNoAttributes(5);
        assertEquals(5, sparseInstance.noAttributes());
    }

    @Test
    public void testConstructorWithNoAttributes() {
        sparseInstance = new SparseInstance(3);
        assertEquals(3, sparseInstance.noAttributes());
    }

    @Test
    public void testInitiateMethod() {
        double[] dataVector = {1.0, 0.0, 2.0};
        sparseInstance = new SparseInstance();
        sparseInstance.initiate(dataVector);
        assertEquals(2, sparseInstance.size());
    }

    // Add more test cases for other methods as needed

}