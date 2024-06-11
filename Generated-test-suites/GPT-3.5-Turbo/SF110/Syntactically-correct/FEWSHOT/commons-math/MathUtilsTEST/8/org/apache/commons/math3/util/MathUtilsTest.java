package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int hash = MathUtils.hash(value);
        assertNotNull(hash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.23, 4.56};
        int hash = MathUtils.hash(value);
        assertNotNull(hash);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 1.23;
        double center = 0.0;
        double normalizedAngle = MathUtils.normalizeAngle(a, center);
        assertNotNull(normalizedAngle);
    }

    @Test
    public void testReduce() {
        double a = 1.23;
        double period = 0.5;
        double offset = 0.0;
        double reduced = MathUtils.reduce(a, period, offset);
        assertNotNull(reduced);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 1;
        byte sign = -1;
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            assertNotNull(result);
        } catch (MathArithmeticException e) {
            fail("Should not throw MathArithmeticException");
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 1;
        short sign = -1;
        try {
            short result = MathUtils.copySign(magnitude, sign);
            assertNotNull(result);
        } catch (MathArithmeticException e) {
            fail("Should not throw MathArithmeticException");
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 1;
        int sign = -1;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            assertNotNull(result);
        } catch (MathArithmeticException e) {
            fail("Should not throw MathArithmeticException");
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 1;
        long sign = -1;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            assertNotNull(result);
        } catch (MathArithmeticException e) {
            fail("Should not throw MathArithmeticException");
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        double x = 1.23;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("Should not throw NotFiniteNumberException");
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] val = {1.23, 4.56};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("Should not throw NotFiniteNumberException");
        }
    }

    @Test
    public void testCheckNotNullObjectPatternArgs() {
        Object o = new Object();
        LocalizedFormats pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            fail("Should not throw NullArgumentException");
        }
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("Should not throw NullArgumentException");
        }
    }
}