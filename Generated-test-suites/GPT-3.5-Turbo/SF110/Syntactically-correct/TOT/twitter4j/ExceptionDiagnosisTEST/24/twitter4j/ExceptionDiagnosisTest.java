import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionDiagnosisTest {

   private ExceptionDiagnosis exceptionDiagnosis;

   @Before
   public void setUp() {
       exceptionDiagnosis = new ExceptionDiagnosis(new Throwable());
   }

   @Test
   public void testGetStackLineHash() {
       int stackLineHash = exceptionDiagnosis.getStackLineHash();
       assertEquals(0, stackLineHash);
   }

   @Test
   public void testGetLineNumberHash() {
       int lineNumberHash = exceptionDiagnosis.getLineNumberHash();
       assertEquals(0, lineNumberHash);
   }

   @Test
   public void testAsHexString() {
       String hexString = exceptionDiagnosis.asHexString();
       assertNotNull(hexString);
   }

   @Test
   public void testEquals() {
       ExceptionDiagnosis diagnosis1 = new ExceptionDiagnosis(new Throwable());
       ExceptionDiagnosis diagnosis2 = new ExceptionDiagnosis(new Throwable());

       assertTrue(diagnosis1.equals(diagnosis2));
   }

   @Test
   public void testHashCode() {
       int hashCode1 = exceptionDiagnosis.hashCode();
       ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(new Throwable());
       int hashCode2 = diagnosis.hashCode();

       assertEquals(hashCode1, hashCode2);
   }

   @Test
   public void testToString() {
       String toString = exceptionDiagnosis.toString();
       assertNotNull(toString);
   }
}