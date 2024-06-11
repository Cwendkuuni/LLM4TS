import org.junit.*;
import static org.junit.Assert.*;

public class ProbeInformationTest {

   ProbeInformation probe;

   @Before
   public void setUp() {
      probe = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "XMLRPCUrl");
   }

   @Test
   public void testGetTargetType() {
      assertEquals("TargetType", probe.getTargetType());
   }

   @Test
   public void testGetBmName() {
      assertEquals("BMName", probe.getBmName());
   }

   @Test
   public void testGetProbeName() {
      assertEquals("ProbeName", probe.getProbeName());
   }

   @Test
   public void testGetTargetName() {
      assertEquals("TargetName", probe.getTargetName());
   }

   @Test
   public void testGetBmClass() {
      assertEquals("BMClass", probe.getBmClass());
   }

   @Test
   public void testGetBmDescription() {
      assertEquals("BMDescription", probe.getBmDescription());
   }

   @Test
   public void testGetPrecision() {
      assertEquals(10, probe.getPrecision());
   }

   @Test
   public void testGetMeasureURI() {
      String expectedURI = "createMeasureURI(TargetType, TargetName, BMClass, BMName)";
      assertEquals(expectedURI, probe.getMeasureURI());
   }

   @Test
   public void testGetXmlRpcUrl() {
      assertEquals("XMLRPCUrl", probe.getXmlRpcUrl());
   }

   @Test
   public void testEquals() {
      ProbeInformation probe2 = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "XMLRPCUrl");
      assertTrue(probe.equals(probe2));
   }

   @Test
   public void testNotEquals_TargetName() {
      ProbeInformation probe2 = new ProbeInformation("DifferentName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "XMLRPCUrl");
      assertFalse(probe.equals(probe2));
   }

   @Test
   public void testNotEquals_TargetType() {
      ProbeInformation probe2 = new ProbeInformation("TargetName", "DifferentType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "XMLRPCUrl");
      assertFalse(probe.equals(probe2));
   }

   @Test
   public void testNotEquals_BmClass() {
      ProbeInformation probe2 = new ProbeInformation("TargetName", "TargetType", "DifferentClass", "BMName", "BMDescription", "ProbeName", 10, "XMLRPCUrl");
      assertFalse(probe.equals(probe2));
   }

   @Test
   public void testNotEquals_BmName() {
      ProbeInformation probe2 = new ProbeInformation("TargetName", "TargetType", "BMClass", "DifferentName", "BMDescription", "ProbeName", 10, "XMLRPCUrl");
      assertFalse(probe.equals(probe2));
   }

   @Test
   public void testNotEquals_BmDescription() {
      ProbeInformation probe2 = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "DifferentDescription", "ProbeName", 10, "XMLRPCUrl");
      assertFalse(probe.equals(probe2));
   }

   @Test
   public void testNotEquals_ProbeName() {
      ProbeInformation probe2 = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "DifferentProbeName", 10, "XMLRPCUrl");
      assertFalse(probe.equals(probe2));
   }

   @Test
   public void testNotEquals_Precision() {
      ProbeInformation probe2 = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 20, "XMLRPCUrl");
      assertFalse(probe.equals(probe2));
   }

   @Test
   public void testNotEquals_NullObject() {
      assertFalse(probe.equals(null));
   }

   @Test
   public void testHashCode() {
      ProbeInformation probe2 = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 10, "XMLRPCUrl");
      assertEquals(probe.hashCode(), probe2.hashCode());
   }

}