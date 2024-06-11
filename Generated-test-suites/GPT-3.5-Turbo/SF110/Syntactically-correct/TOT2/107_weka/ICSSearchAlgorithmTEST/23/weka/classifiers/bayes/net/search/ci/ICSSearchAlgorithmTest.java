package weka.classifiers.bayes.net.search.ci;
import org.junit.*;
import static org.junit.Assert.*;

public class ICSSearchAlgorithmTest {

  @Test
  public void testMaxn() {
    ICSSearchAlgorithm icsSearchAlgorithm = new ICSSearchAlgorithm();
    assertEquals(icsSearchAlgorithm.maxn(), icsSearchAlgorithm.m_instances.numAttributes());
  }

  @Test
  public void testSetMaxCardinality() {
    ICSSearchAlgorithm icsSearchAlgorithm = new ICSSearchAlgorithm();
    icsSearchAlgorithm.setMaxCardinality(3);
    assertEquals(icsSearchAlgorithm.getMaxCardinality(), 3);
  }

  @Test
  public void testCalcDependencyGraph() {
    ICSSearchAlgorithm icsSearchAlgorithm = new ICSSearchAlgorithm();
    boolean[][] edges = new boolean[icsSearchAlgorithm.maxn() + 1][icsSearchAlgorithm.maxn()];
    ICSSearchAlgorithm.SeparationSet[][] sepsets = new ICSSearchAlgorithm.SeparationSet[icsSearchAlgorithm.maxn() + 1][icsSearchAlgorithm.maxn()];
    icsSearchAlgorithm.calcDependencyGraph(edges, sepsets);
    assertNotNull(edges);
    assertNotNull(sepsets);
  }
}