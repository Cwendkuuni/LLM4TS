import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

public class GammaTest {

    private static final double DELTA = 1e-15;
    
    @Test
    public void testLogGamma() {
        double[] inputs = {-1, 0, 1, 2, 3, 4, 5};
        double[] expected = {Double.NaN, Double.POSITIVE_INFINITY, 0, 0.6931471805599453, 1.791759469228055, 3.178053830347945, 4.852030263919617};
        
        for (int i = 0; i < inputs.length; i++) {
            assertEquals(expected[i], Gamma.logGamma(inputs[i]), DELTA);
        }
    }
    
    @Test
    public void testRegularizedGammaP() throws MathException {
        double[] aValues = {-1, 0, 1, 2, 3};
        double[] xValues = {-1, 0, 1, 2, 3};
        double[][] expected = {
            {Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN},
            {1, 0, 0, 0, 0},
            {0, Double.POSITIVE_INFINITY, 1, 0.2642411176571153, 0.010827308368709114},
            {1, 1, 0.4048585115303378, 0.15425955521645815, 0.006703438221418105},
            {1, 1, 1, 0.6321205588285577, 0.09516258196404048}
        };
        
        for (int i = 0; i < aValues.length; i++) {
            for (int j = 0; j < xValues.length; j++) {
                assertEquals(expected[i][j], Gamma.regularizedGammaP(aValues[i], xValues[j]), DELTA);
            }
        }
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        double[] aValues = {-1, 0, 1, 2, 3};
        double[] xValues = {-1, 0, 1, 2, 3};
        double[][] expected = {
            {Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN},
            {0, 1, 1, 1, 1},
            {1, 0, 0, 0.7357588823428847, 0.9891726916312909},
            {0, 0, 0.5951414884696622, 0.8457404447835418, 0.9932965617785819},
            {0, 0, 0, 0.36787944117144233, 0.9048374180359595}
        };
        
        for (int i = 0; i < aValues.length; i++) {
            for (int j = 0; j < xValues.length; j++) {
                assertEquals(expected[i][j], Gamma.regularizedGammaQ(aValues[i], xValues[j]), DELTA);
            }
        }
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaP_maxIterationsExceeded() throws MathException {
        Gamma.regularizedGammaP(1, 1, 1e-9, 5);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaQ_maxIterationsExceeded() throws MathException {
        Gamma.regularizedGammaQ(1, 1, 1e-9, 5);
    }
}