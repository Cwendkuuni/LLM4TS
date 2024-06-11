import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BigIntegerMathTest {

    private final BigIntegerMath.BigIntegerFunction function;
    private final BigInteger expectedResult;
    private final BigInteger input;

    public BigIntegerMathTest(BigIntegerMath.BigIntegerFunction function, BigInteger expectedResult, BigInteger input) {
        this.function = function;
        this.expectedResult = expectedResult;
        this.input = input;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {BigIntegerMath::isPowerOfTwo, true, BigInteger.valueOf(8)},
                {BigIntegerMath::isPowerOfTwo, false, BigInteger.valueOf(9)},
                {BigIntegerMath::log2, BigInteger.valueOf(3), BigInteger.valueOf(8)},
                {BigIntegerMath::log10, BigInteger.valueOf(1), BigInteger.valueOf(10)},
                {BigIntegerMath::sqrt, BigInteger.valueOf(3), BigInteger.valueOf(9)},
                {BigIntegerMath::divide, BigInteger.valueOf(3), BigInteger.valueOf(9)}
        });
    }

    @Test
    public void test() {
        assertEquals(expectedResult, function.apply(input));
    }

    interface BigIntegerFunction {
        BigInteger apply(BigInteger x);
    }
}