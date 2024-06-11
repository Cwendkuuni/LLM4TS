import org.junit.*;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testGetStackLineHash() {
        // Test case 1
        Throwable th1 = new Throwable();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        int expected1 = 0;
        int actual1 = ed1.getStackLineHash();
        assertEquals(expected1, actual1);

        // Test case 2
        Throwable th2 = new Throwable();
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        int expected2 = 0;
        int actual2 = ed2.getStackLineHash();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testGetLineNumberHash() {
        // Test case 1
        Throwable th1 = new Throwable();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        int expected1 = 0;
        int actual1 = ed1.getLineNumberHash();
        assertEquals(expected1, actual1);

        // Test case 2
        Throwable th2 = new Throwable();
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        int expected2 = 0;
        int actual2 = ed2.getLineNumberHash();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testAsHexString() {
        // Test case 1
        Throwable th1 = new Throwable();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        String expected1 = "";
        String actual1 = ed1.asHexString();
        assertEquals(expected1, actual1);

        // Test case 2
        Throwable th2 = new Throwable();
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        String expected2 = "";
        String actual2 = ed2.asHexString();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testToHexString() {
        // Test case 1
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(new Throwable());
        int value1 = 0;
        String expected1 = "00000000";
        String actual1 = ed1.toHexString(value1);
        assertEquals(expected1, actual1);

        // Test case 2
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(new Throwable());
        int value2 = 0;
        String expected2 = "00000000";
        String actual2 = ed2.toHexString(value2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testEquals() {
        // Test case 1
        Throwable th1 = new Throwable();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        Throwable th2 = new Throwable();
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        boolean expected1 = false;
        boolean actual1 = ed1.equals(ed2);
        assertEquals(expected1, actual1);

        // Test case 2
        Throwable th3 = new Throwable();
        ExceptionDiagnosis ed3 = new ExceptionDiagnosis(th3);
        Throwable th4 = new Throwable();
        ExceptionDiagnosis ed4 = new ExceptionDiagnosis(th4);
        boolean expected2 = false;
        boolean actual2 = ed3.equals(ed4);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testHashCode() {
        // Test case 1
        Throwable th1 = new Throwable();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        int expected1 = 0;
        int actual1 = ed1.hashCode();
        assertEquals(expected1, actual1);

        // Test case 2
        Throwable th2 = new Throwable();
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        int expected2 = 0;
        int actual2 = ed2.hashCode();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testToString() {
        // Test case 1
        Throwable th1 = new Throwable();
        ExceptionDiagnosis ed1 = new ExceptionDiagnosis(th1);
        String expected1 = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        String actual1 = ed1.toString();
        assertEquals(expected1, actual1);

        // Test case 2
        Throwable th2 = new Throwable();
        ExceptionDiagnosis ed2 = new ExceptionDiagnosis(th2);
        String expected2 = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        String actual2 = ed2.toString();
        assertEquals(expected2, actual2);
    }
}