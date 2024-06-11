import org.junit.Test;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;

public class EmbeddedRungeKuttaIntegratorTest {

   @Test
   public void testGetSafety() {
      EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
      double safety = integrator.getSafety();
      Assert.assertEquals(0.9, safety, 0.01);
   }

   @Test
   public void testSetSafety() {
      EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
      integrator.setSafety(0.8);
      double safety = integrator.getSafety();
      Assert.assertEquals(0.8, safety, 0.01);
   }

   @Test
   public void testIntegrate() {
      // TODO: Implement test for integrate method
   }

   @Test
   public void testGetMinReduction() {
      EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
      double minReduction = integrator.getMinReduction();
      Assert.assertEquals(0.2, minReduction, 0.01);
   }

   @Test
   public void testSetMinReduction() {
      EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
      integrator.setMinReduction(0.1);
      double minReduction = integrator.getMinReduction();
      Assert.assertEquals(0.1, minReduction, 0.01);
   }

   @Test
   public void testGetMaxGrowth() {
      EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
      double maxGrowth = integrator.getMaxGrowth();
      Assert.assertEquals(10.0, maxGrowth, 0.01);
   }

   @Test
   public void testSetMaxGrowth() {
      EmbeddedRungeKuttaIntegrator integrator = new FakeEmbeddedRungeKuttaIntegrator();
      integrator.setMaxGrowth(20.0);
      double maxGrowth = integrator.getMaxGrowth();
      Assert.assertEquals(20.0, maxGrowth, 0.01);
   }

   @Test
   public void testEstimateError() {
      // TODO: Implement test for estimateError method
   }

}