package twitter4j; 

import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testConstructor1() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testConstructor2() {
        String[] inclusionFilter = {"filter"};
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable(), inclusionFilter);
        assertNotNull(diagnosis);
        assertEquals(0, diagnosis.getStackLineHash());
        assertEquals(0, diagnosis.getLineNumberHash());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testGetStackLineHash() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        assertEquals(0, diagnosis.getStackLineHash());
    }

    @Test
    public void testGetLineNumberHash() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        assertEquals(0, diagnosis.getLineNumberHash());
    }

    @Test
    public void testAsHexString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        assertEquals("", diagnosis.asHexString());
    }

    @Test
    public void testEquals() {
        ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new Throwable());
        ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new Throwable());
        assertEquals(true, diagnosis1.equals(diagnosis2));
    }

    @Test
    public void testHashCode() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        int expectedHashCode = diagnosis.getStackLineHash() * 31 + diagnosis.getLineNumberHash();
        assertEquals(expectedHashCode, diagnosis.hashCode());
    }

    @Test
    public void testToString() {
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
        String expectedString = "ExceptionDiagnosis{stackLineHash=" + diagnosis.getStackLineHash() +
                ", lineNumberHash=" + diagnosis.getLineNumberHash() + "}";
        assertEquals(expectedString, diagnosis.toString());
    }
}