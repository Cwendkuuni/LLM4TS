package twitter4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExceptionDiagnosisTest {
    
    @Test
    public void testExceptionDiagnosis() {
        Throwable throwable1 = new Throwable();
        ExceptionDiagnosis exceptionDiagnosis1 = new ExceptionDiagnosis(throwable1);
        assertEquals(0, exceptionDiagnosis1.getStackLineHash());
        assertEquals(0, exceptionDiagnosis1.getLineNumberHash());
        assertEquals("", exceptionDiagnosis1.asHexString());
        
        Throwable throwable2 = new Throwable();
        throwable2.initCause(throwable1);
        ExceptionDiagnosis exceptionDiagnosis2 = new ExceptionDiagnosis(throwable2);
        assertEquals(0, exceptionDiagnosis2.getStackLineHash());
        assertEquals(0, exceptionDiagnosis2.getLineNumberHash());
        assertEquals("", exceptionDiagnosis2.asHexString());
        
        String[] inclusionFilter = {"twitter4j"};
        ExceptionDiagnosis exceptionDiagnosis3 = new ExceptionDiagnosis(throwable1, inclusionFilter);
        assertEquals(throwable1.getStackTrace().hashCode(), exceptionDiagnosis3.getStackLineHash());
        assertEquals(throwable1.getStackTrace()[0].getLineNumber(), exceptionDiagnosis3.getLineNumberHash());
        assertEquals(exceptionDiagnosis3.toHexString(throwable1.getStackTrace().hashCode()) + "-" + exceptionDiagnosis3.toHexString(throwable1.getStackTrace()[0].getLineNumber()), exceptionDiagnosis3.asHexString());
        
        ExceptionDiagnosis exceptionDiagnosis4 = new ExceptionDiagnosis(throwable2, inclusionFilter);
        assertEquals(throwable2.getStackTrace().hashCode() + throwable1.getStackTrace().hashCode(), exceptionDiagnosis4.getStackLineHash());
        assertEquals(throwable2.getStackTrace()[0].getLineNumber() + throwable1.getStackTrace()[0].getLineNumber(), exceptionDiagnosis4.getLineNumberHash());
        assertEquals(exceptionDiagnosis4.toHexString(throwable2.getStackTrace().hashCode() + throwable1.getStackTrace().hashCode()) + "-" + exceptionDiagnosis4.toHexString(throwable2.getStackTrace()[0].getLineNumber() + throwable1.getStackTrace()[0].getLineNumber()), exceptionDiagnosis4.asHexString());
    }
}