import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.apache.commons.math.MathException;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Typical use case
        double result1 = Gamma.logGamma(5);
        assertThat(result1, is(closeTo(3.1780538303479458, 1e-9)));

        // Edge case
        double result2 = Gamma.logGamma(1);
        assertThat(result2, is(closeTo(0.0, 1e-9)));

        // Error scenario
        double result3 = Gamma.logGamma(-2);
        assertThat(result3, is(Double.NaN));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Typical use case
        double result1 = Gamma.regularizedGammaP(3, 5);
        assertThat(result1, is(closeTo(0.6285729955594609, 1e-9)));

        // Edge case
        double result2 = Gamma.regularizedGammaP(1, 0);
        assertThat(result2, is(closeTo(0.0, 1e-9)));

        // Error scenario
        double result3 = Gamma.regularizedGammaP(-1, 3);
        assertThat(result3, is(Double.NaN));
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Typical use case
        double result1 = Gamma.regularizedGammaQ(3, 5);
        assertThat(result1, is(closeTo(0.37142700444053914, 1e-9)));

        // Edge case
        double result2 = Gamma.regularizedGammaQ(1, 0);
        assertThat(result2, is(closeTo(1.0, 1e-9)));

        // Error scenario
        double result3 = Gamma.regularizedGammaQ(-1, 3);
        assertThat(result3, is(Double.NaN));
    }

}