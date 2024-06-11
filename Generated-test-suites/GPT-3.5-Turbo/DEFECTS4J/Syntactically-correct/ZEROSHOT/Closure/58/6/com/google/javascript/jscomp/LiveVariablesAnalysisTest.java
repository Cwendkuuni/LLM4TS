import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

   @Test
   public void testCreateEntryLattice() {
      LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
      assertNotNull(lattice);
   }

   @Test
   public void testCreateInitialEstimateLattice() {
      LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
      assertNotNull(lattice);
   }

   @Test
   public void testFlowThrough() {
      Node node = new Node(Token.SCRIPT);
      LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
      LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
      LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
      assertNotNull(result);
   }

   @Test
   public void testGetEscapedLocals() {
      LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
      assertNotNull(analysis.getEscapedLocals());
   }

   @Test
   public void testGetVarIndex() {
      LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
      int index = analysis.getVarIndex("var");
      assertEquals(0, index);
   }

   @Test
   public void testIsForward() {
      LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
      boolean isForward = analysis.isForward();
      assertFalse(isForward);
   }

   @Test
   public void testLiveVariableJoinOp() {
      LiveVariablesAnalysis.LiveVariableJoinOp joinOp = new LiveVariablesAnalysis.LiveVariableJoinOp();
      assertNotNull(joinOp);
   }

   @Test
   public void testLiveVariableLatticeEquals() {
      LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(10);
      LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(10);
      boolean equals = lattice1.equals(lattice2);
      assertTrue(equals);
   }

   @Test
   public void testLiveVariableLatticeToString() {
      LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
      String str = lattice.toString();
      assertNotNull(str);
   }

   @Test
   public void testLiveVariableLatticeHashCode() {
      LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
      int hashCode = lattice.hashCode();
      assertEquals(0, hashCode);
   }
}