import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Assert;
import org.junit.Test;

public class MannWhitneyUTestTest {
  
@Test
public void testMannWhitneyU() throws NullArgumentException, NoDataException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};

    double result = test.mannWhitneyU(x, y);
    Assert.assertEquals(0, result, 0);
}

@Test(expected = NullArgumentException.class)
public void testMannWhitneyUWithNullX() throws NullArgumentException, NoDataException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = null;
    double[] y = {6, 7, 8, 9, 10};

    test.mannWhitneyU(x, y);
}

@Test(expected = NullArgumentException.class)
public void testMannWhitneyUWithNullY() throws NullArgumentException, NoDataException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = null;

    test.mannWhitneyU(x, y);
}

@Test(expected = NoDataException.class)
public void testMannWhitneyUWithEmptyX() throws NullArgumentException, NoDataException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {6, 7, 8, 9, 10};

    test.mannWhitneyU(x, y);
}

@Test(expected = NoDataException.class)
public void testMannWhitneyUWithEmptyY() throws NullArgumentException, NoDataException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {};

    test.mannWhitneyU(x, y);
}

@Test
public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {6, 7, 8, 9, 10};

    double result = test.mannWhitneyUTest(x, y);
    Assert.assertEquals(1, result, 0.001);
}

@Test(expected = NullArgumentException.class)
public void testMannWhitneyUTestWithNullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = null;
    double[] y = {6, 7, 8, 9, 10};

    test.mannWhitneyUTest(x, y);
}

@Test(expected = NullArgumentException.class)
public void testMannWhitneyUTestWithNullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = null;

    test.mannWhitneyUTest(x, y);
}

@Test(expected = NoDataException.class)
public void testMannWhitneyUTestWithEmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {};
    double[] y = {6, 7, 8, 9, 10};

    test.mannWhitneyUTest(x, y);
}

@Test(expected = NoDataException.class)
public void testMannWhitneyUTestWithEmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {};

    test.mannWhitneyUTest(x, y);
}

@Test
public void testMannWhitneyUTestWithEdgeCase() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
    MannWhitneyUTest test = new MannWhitneyUTest();
    double[] x = {1, 2, 3, 4, 5};
    double[] y = {5, 5, 5, 5, 5};

    double result = test.mannWhitneyUTest(x, y);
    Assert.assertEquals(0.2637, result, 0.001);
}
}