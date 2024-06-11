import org.junit.*;

public class ExceptionDiagnosisTest {

    @Test
    public void testExceptionDiagnosisConstructor() {
        Throwable th = new RuntimeException("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        Assert.assertEquals(0, diag.getStackLineHash());
        Assert.assertEquals(0, diag.getLineNumberHash());
    }
    
    @Test
    public void testExceptionDiagnosisConstructorWithInclusionFilter() {
        Throwable th = new NullPointerException();
        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th, inclusionFilter);
        Assert.assertEquals(0, diag.getStackLineHash());
        Assert.assertEquals(0, diag.getLineNumberHash());
    }
    
    @Test
    public void testEquals() {
        Throwable th1 = new RuntimeException();
        Throwable th2 = new IllegalStateException();
        ExceptionDiagnosis diag1 = new ExceptionDiagnosis(th1);
        ExceptionDiagnosis diag2 = new ExceptionDiagnosis(th2);
        Assert.assertNotEquals(diag1, diag2);
    }
    
    @Test
    public void testHashCode() {
        Throwable th = new IllegalArgumentException();
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        Assert.assertNotEquals(0, diag.hashCode());
    }
    
    @Test
    public void testToString() {
        Throwable th = new Exception("Test Exception");
        ExceptionDiagnosis diag = new ExceptionDiagnosis(th);
        String expected = "ExceptionDiagnosis{stackLineHash=0, lineNumberHash=0}";
        Assert.assertEquals(expected, diag.toString());
    }
}