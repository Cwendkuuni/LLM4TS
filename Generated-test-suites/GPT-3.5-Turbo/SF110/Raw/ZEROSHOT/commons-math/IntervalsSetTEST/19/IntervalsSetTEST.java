import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IntervalsSetTest {

   private IntervalsSet intervalsSet;

   @Before
   public void setUp() {
      this.intervalsSet = new IntervalsSet();
   }

   @Test
   public void testConstructor1() {
      IntervalsSet intervalsSet = new IntervalsSet();
      assertEquals(intervalsSet.getInf(), Double.POSITIVE_INFINITY, 0.0001);
      assertEquals(intervalsSet.getSup(), Double.NEGATIVE_INFINITY, 0.0001);
   }

   @Test
   public void testConstructor2() {
      IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
      List<Interval> expectedList = Arrays.asList(new Interval(1.0, 2.0));
      assertEquals(expectedList, intervalsSet.asList());
   }

   @Test
   public void testConstructor3() {
      BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
      IntervalsSet intervalsSet = new IntervalsSet(tree);
      assertEquals(intervalsSet.getInf(), Double.POSITIVE_INFINITY, 0.0001);
      assertEquals(intervalsSet.getSup(), Double.NEGATIVE_INFINITY, 0.0001);
   }

   @Test
   public void testConstructor4() {
      List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
      IntervalsSet intervalsSet = new IntervalsSet(boundary);
      assertEquals(intervalsSet.getInf(), Double.POSITIVE_INFINITY, 0.0001);
      assertEquals(intervalsSet.getSup(), Double.NEGATIVE_INFINITY, 0.0001);
   }

   @Test
   public void testBuildNew() {
      BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
      IntervalsSet newSet = intervalsSet.buildNew(tree);
      assertEquals(newSet.getInf(), Double.POSITIVE_INFINITY, 0.0001);
      assertEquals(newSet.getSup(), Double.NEGATIVE_INFINITY, 0.0001);
   }

   @Test
   public void testComputeGeometricalProperties() {
      intervalsSet.computeGeometricalProperties();
      assertEquals(intervalsSet.getInf(), Double.POSITIVE_INFINITY, 0.0001);
      assertEquals(intervalsSet.getSup(), Double.NEGATIVE_INFINITY, 0.0001);
   }

   @Test
   public void testGetInf() {
      double inf = intervalsSet.getInf();
      assertEquals(inf, Double.POSITIVE_INFINITY, 0.0001);
   }

   @Test
   public void testGetSup() {
      double sup = intervalsSet.getSup();
      assertEquals(sup, Double.NEGATIVE_INFINITY, 0.0001);
   }

   @Test
   public void testAsList() {
      List<Interval> list = intervalsSet.asList();
      List<Interval> expectedList = new ArrayList<>();
      assertEquals(expectedList, list);
   }

   @Test
   public void testRecurseList() {
      BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
      List<Interval> list = new ArrayList<>();
      double lower = Double.NEGATIVE_INFINITY;
      double upper = Double.POSITIVE_INFINITY;
      intervalsSet.recurseList(tree, list, lower, upper);
      List<Interval> expectedList = new ArrayList<>();
      assertEquals(expectedList, list);
   }
}