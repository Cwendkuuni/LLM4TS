package JSci.maths;

public final class SpecialMath extends AbstractMath implements NumericalConstants
{
    private static final double EPS = 2.22E-16;
    private static final double XMININ = 2.23E-308;
    private static final double[] ai0cs;
    private static final double[] ai02cs;
    private static final double[] ai1cs;
    private static final double[] ai12cs;
    private static final double[] aifcs;
    private static final double[] aigcs;
    private static final double[] aipcs;
    private static final double[] am21cs;
    private static final double[] ath1cs;
    private static final double[] am22cs;
    private static final double[] ath2cs;
    private static final double[] bi0cs;
    private static final double[] bj0cs;
    private static final double[] bm0cs;
    private static final double[] bth0cs;
    private static final double[] by0cs;
    private static final double[] bi1cs;
    private static final double[] bj1cs;
    private static final double[] bm1cs;
    private static final double[] bth1cs;
    private static final double[] by1cs;
    private static final double LOGSQRT2PI;
    private static final double[] g_p;
    private static final double[] g_q;
    private static final double[] g_c;
    public static final double GAMMA_X_MAX_VALUE = 171.624;
    public static final double LOG_GAMMA_X_MAX_VALUE = 2.55E305;
    private static final double lg_d1 = -0.5772156649015329;
    private static final double lg_d2 = 0.42278433509846713;
    private static final double lg_d4 = 1.791759469228055;
    private static final double[] lg_p1;
    private static final double[] lg_q1;
    private static final double[] lg_p2;
    private static final double[] lg_q2;
    private static final double[] lg_p4;
    private static final double[] lg_q4;
    private static final double[] lg_c;
    private static final double lg_frtbig = 2.25E76;
    private static final double pnt68 = 0.6796875;
    private static final ThreadLocal logGammaCache_res;
    private static final ThreadLocal logGammaCache_x;
    private static final int MAX_ITERATIONS = 1000000000;
    private static final double PRECISION = 8.88E-16;
    private static final ThreadLocal logBetaCache_res;
    private static final ThreadLocal logBetaCache_p;
    private static final ThreadLocal logBetaCache_q;
    private static final double e_efx = 0.1283791670955126;
    private static final double[] ePp;
    private static final double[] eQq;
    private static final double[] ePa;
    private static final double[] eQa;
    private static final double e_erx = 0.8450629115104675;
    private static final double[] eRa;
    private static final double[] eSa;
    private static final double[] eRb;
    private static final double[] eSb;
    
    public static double chebyshev(final double x, final double[] series) {
        double b0 = 0.0;
        double b2 = 0.0;
        double b3 = 0.0;
        final double twox = 2.0 * x;
        for (int i = series.length - 1; i > -1; --i) {
            b3 = b2;
            b2 = b0;
            b0 = twox * b2 - b3 + series[i];
        }
        return 0.5 * (b0 - b3);
    }
    
    public static double airy(final double x) {
        if (x < -1.0) {
            return airyModPhase(x);
        }
        if (x > 1.0) {
            return expAiry(x) * Math.exp(-2.0 * x * Math.sqrt(x) / 3.0);
        }
        final double z = x * x * x;
        return 0.375 + (chebyshev(z, SpecialMath.aifcs) - x * (0.25 + chebyshev(z, SpecialMath.aigcs)));
    }
    
    private static double airyModPhase(final double x) {
        double modulus;
        double phase;
        if (x < -2.0) {
            final double z = 16.0 / (x * x * x) + 1.0;
            modulus = 0.3125 + chebyshev(z, SpecialMath.am21cs);
            phase = -0.625 + chebyshev(z, SpecialMath.ath1cs);
        }
        else {
            final double z = (16.0 / (x * x * x) + 9.0) / 7.0;
            modulus = 0.3125 + chebyshev(z, SpecialMath.am22cs);
            phase = -0.625 + chebyshev(z, SpecialMath.ath2cs);
        }
        final double sqrtx = Math.sqrt(-x);
        modulus = Math.sqrt(modulus / sqrtx);
        phase = 0.7853981633974483 - x * sqrtx * phase;
        return modulus * Math.cos(phase);
    }
    
    private static double expAiry(final double x) {
        if (x < -1.0) {
            return airyModPhase(x);
        }
        if (x <= 1.0) {
            final double z = x * x * x;
            return 0.375 + (chebyshev(z, SpecialMath.aifcs) - x * (0.25 + chebyshev(z, SpecialMath.aigcs))) * Math.exp(2.0 * x * Math.sqrt(x) / 3.0);
        }
        final double sqrtx = Math.sqrt(x);
        final double z2 = 2.0 / (x * sqrtx) - 1.0;
        return (0.28125 + chebyshev(z2, SpecialMath.aipcs)) / Math.sqrt(sqrtx);
    }
    
    public static double besselFirstZero(final double x) {
        final double y = Math.abs(x);
        if (y > 4.0) {
            final double z = 32.0 / (y * y) - 1.0;
            final double amplitude = (0.75 + chebyshev(z, SpecialMath.bm0cs)) / Math.sqrt(y);
            final double theta = y - 0.7853981633974483 + chebyshev(z, SpecialMath.bth0cs) / y;
            return amplitude * Math.cos(theta);
        }
        if (y == 0.0) {
            return 1.0;
        }
        return chebyshev(0.125 * y * y - 1.0, SpecialMath.bj0cs);
    }
    
    public static double modBesselFirstZero(final double x) {
        final double y = Math.abs(x);
        if (y > 3.0) {
            return Math.exp(y) * expModBesselFirstZero(x);
        }
        return 2.75 + chebyshev(y * y / 4.5 - 1.0, SpecialMath.bi0cs);
    }
    
    private static double expModBesselFirstZero(final double x) {
        final double y = Math.abs(x);
        if (y <= 3.0) {
            return Math.exp(-y) * (2.75 + chebyshev(y * y / 4.5 - 1.0, SpecialMath.bi0cs));
        }
        if (y > 8.0) {
            return (0.375 + chebyshev(16.0 / y - 1.0, SpecialMath.ai02cs)) / Math.sqrt(y);
        }
        return (0.375 + chebyshev((48.0 / y - 11.0) / 5.0, SpecialMath.ai0cs)) / Math.sqrt(y);
    }
    
    public static double besselFirstOne(final double x) {
        final double y = Math.abs(x);
        if (y > 4.0) {
            final double z = 32.0 / (y * y) - 1.0;
            final double amplitude = (0.75 + chebyshev(z, SpecialMath.bm1cs)) / Math.sqrt(y);
            final double theta = y - 2.356194490192345 + chebyshev(z, SpecialMath.bth1cs) / y;
            return Math.abs(amplitude) * x * Math.cos(theta) / Math.abs(x);
        }
        if (y == 0.0) {
            return 0.0;
        }
        return x * (0.25 + chebyshev(0.125 * y * y - 1.0, SpecialMath.bj1cs));
    }
    
    public static double modBesselFirstOne(final double x) {
        final double y = Math.abs(x);
        if (y > 3.0) {
            return Math.exp(y) * expModBesselFirstOne(x);
        }
        if (y == 0.0) {
            return 0.0;
        }
        return x * (0.875 + chebyshev(y * y / 4.5 - 1.0, SpecialMath.bi1cs));
    }
    
    private static double expModBesselFirstOne(final double x) {
        final double y = Math.abs(x);
        if (y > 3.0) {
            if (y > 8.0) {
                return x / y * (0.375 + chebyshev(16.0 / y - 1.0, SpecialMath.ai12cs)) / Math.sqrt(y);
            }
            return x / y * (0.375 + chebyshev((48.0 / y - 11.0) / 5.0, SpecialMath.ai1cs)) / Math.sqrt(y);
        }
        else {
            if (y == 0.0) {
                return 0.0;
            }
            return Math.exp(-y) * x * (0.875 + chebyshev(y * y / 4.5 - 1.0, SpecialMath.bi1cs));
        }
    }
    
    public static double besselSecondZero(final double x) {
        if (x > 4.0) {
            final double z = 32.0 / (x * x) - 1.0;
            final double amplitude = (0.75 + chebyshev(z, SpecialMath.bm0cs)) / Math.sqrt(x);
            final double theta = x - 0.7853981633974483 + chebyshev(z, SpecialMath.bth0cs) / x;
            return amplitude * Math.sin(theta);
        }
        return (Math.log(0.5) + Math.log(x)) * besselFirstZero(x) + 0.375 + chebyshev(0.125 * x * x - 1.0, SpecialMath.by0cs) * 2.0 / 3.141592653589793;
    }
    
    public static double besselSecondOne(final double x) {
        if (x > 4.0) {
            final double z = 32.0 / (x * x) - 1.0;
            final double amplitude = (0.75 + chebyshev(z, SpecialMath.bm1cs)) / Math.sqrt(x);
            final double theta = x - 2.356194490192345 + chebyshev(z, SpecialMath.bth1cs) / x;
            return amplitude * Math.sin(theta);
        }
        return 2.0 * Math.log(0.5 * x) * besselFirstOne(x) / 3.141592653589793 + (0.5 + chebyshev(0.125 * x * x - 1.0, SpecialMath.by1cs)) / x;
    }
    
    public static double gamma(final double x) {
        double fact = 1.0;
        int n = 0;
        double y = x;
        boolean parity = false;
        if (y <= 0.0) {
            y = -x;
            final double y2 = (int)y;
            final double res = y - y2;
            if (res == 0.0) {
                return Double.MAX_VALUE;
            }
            if (y2 != (int)(y2 * 0.5) * 2.0) {
                parity = true;
            }
            fact = -3.141592653589793 / Math.sin(3.141592653589793 * res);
            ++y;
        }
        double res;
        if (y < 2.22E-16) {
            if (y < 2.23E-308) {
                return Double.MAX_VALUE;
            }
            res = 1.0 / y;
        }
        else if (y < 12.0) {
            final double y2 = y;
            double z;
            if (y < 1.0) {
                z = y;
                ++y;
            }
            else {
                n = (int)y - 1;
                y -= n;
                z = y - 1.0;
            }
            double xnum = 0.0;
            double xden = 1.0;
            for (int i = 0; i < 8; ++i) {
                xnum = (xnum + SpecialMath.g_p[i]) * z;
                xden = xden * z + SpecialMath.g_q[i];
            }
            res = xnum / xden + 1.0;
            if (y2 < y) {
                res /= y2;
            }
            else if (y2 > y) {
                for (int i = 0; i < n; ++i) {
                    res *= y;
                    ++y;
                }
            }
        }
        else {
            if (y > 171.624) {
                return Double.MAX_VALUE;
            }
            final double ysq = y * y;
            double sum = SpecialMath.g_c[6];
            for (int i = 0; i < 6; ++i) {
                sum = sum / ysq + SpecialMath.g_c[i];
            }
            sum = sum / y - y + SpecialMath.LOGSQRT2PI;
            sum += (y - 0.5) * Math.log(y);
            res = Math.exp(sum);
        }
        if (parity) {
            res = -res;
        }
        if (fact != 1.0) {
            res = fact / res;
        }
        return res;
    }
    
    public static double logGamma(final double x) {
        if (x == SpecialMath.logGammaCache_x.get()) {
            return SpecialMath.logGammaCache_res.get();
        }
        double res;
        if (x > 0.0 && x <= 2.55E305) {
            if (x <= 2.22E-16) {
                res = -Math.log(x);
            }
            else if (x <= 1.5) {
                double corr;
                double xm1;
                if (x < 0.6796875) {
                    corr = -Math.log(x);
                    xm1 = x;
                }
                else {
                    corr = 0.0;
                    xm1 = x - 1.0;
                }
                if (x <= 0.5 || x >= 0.6796875) {
                    double xden = 1.0;
                    double xnum = 0.0;
                    for (int i = 0; i < 8; ++i) {
                        xnum = xnum * xm1 + SpecialMath.lg_p1[i];
                        xden = xden * xm1 + SpecialMath.lg_q1[i];
                    }
                    res = corr + xm1 * (-0.5772156649015329 + xm1 * (xnum / xden));
                }
                else {
                    final double xm2 = x - 1.0;
                    double xden = 1.0;
                    double xnum = 0.0;
                    for (int i = 0; i < 8; ++i) {
                        xnum = xnum * xm2 + SpecialMath.lg_p2[i];
                        xden = xden * xm2 + SpecialMath.lg_q2[i];
                    }
                    res = corr + xm2 * (0.42278433509846713 + xm2 * (xnum / xden));
                }
            }
            else if (x <= 4.0) {
                final double xm2 = x - 2.0;
                double xden = 1.0;
                double xnum = 0.0;
                for (int i = 0; i < 8; ++i) {
                    xnum = xnum * xm2 + SpecialMath.lg_p2[i];
                    xden = xden * xm2 + SpecialMath.lg_q2[i];
                }
                res = xm2 * (0.42278433509846713 + xm2 * (xnum / xden));
            }
            else if (x <= 12.0) {
                final double xm3 = x - 4.0;
                double xden = -1.0;
                double xnum = 0.0;
                for (int i = 0; i < 8; ++i) {
                    xnum = xnum * xm3 + SpecialMath.lg_p4[i];
                    xden = xden * xm3 + SpecialMath.lg_q4[i];
                }
                res = 1.791759469228055 + xm3 * (xnum / xden);
            }
            else {
                res = 0.0;
                if (x <= 2.25E76) {
                    res = SpecialMath.lg_c[6];
                    final double ysq = x * x;
                    for (int i = 0; i < 6; ++i) {
                        res = res / ysq + SpecialMath.lg_c[i];
                    }
                }
                res /= x;
                final double corr = Math.log(x);
                res = res + SpecialMath.LOGSQRT2PI - 0.5 * corr;
                res += x * (corr - 1.0);
            }
        }
        else {
            res = Double.MAX_VALUE;
        }
        SpecialMath.logGammaCache_x.set(new Double(x));
        SpecialMath.logGammaCache_res.set(new Double(res));
        return res;
    }
    
    public static double incompleteGamma(final double a, final double x) {
        if (x <= 0.0 || a <= 0.0 || a > 2.55E305) {
            return 0.0;
        }
        if (x < a + 1.0) {
            return gammaSeriesExpansion(a, x);
        }
        return 1.0 - gammaFraction(a, x);
    }
    
    private static double gammaSeriesExpansion(final double a, final double x) {
        double ap = a;
        double sum;
        double del = sum = 1.0 / a;
        for (int n = 1; n < 1000000000; ++n) {
            ++ap;
            del *= x / ap;
            sum += del;
            if (del < sum * 8.88E-16) {
                return sum * Math.exp(-x + a * Math.log(x) - logGamma(a));
            }
        }
        throw new RuntimeException("Maximum iterations exceeded: please file a bug report.");
    }
    
    private static double gammaFraction(final double a, final double x) {
        double b = x + 1.0 - a;
        double c = 4.4843049327354256E307;
        double h;
        double d = h = 1.0 / b;
        double del = 0.0;
        for (int i = 1; i < 1000000000 && Math.abs(del - 1.0) > 8.88E-16; del = d * c, h *= del, ++i) {
            final double an = -i * (i - a);
            b += 2.0;
            d = an * d + b;
            c = b + an / c;
            if (Math.abs(c) < 2.23E-308) {
                c = 2.23E-308;
            }
            if (Math.abs(d) < 2.23E-308) {
                c = 2.23E-308;
            }
            d = 1.0 / d;
        }
        return Math.exp(-x + a * Math.log(x) - logGamma(a)) * h;
    }
    
    public static double beta(final double p, final double q) {
        if (p <= 0.0 || q <= 0.0 || p + q > 2.55E305) {
            return 0.0;
        }
        return Math.exp(logBeta(p, q));
    }
    
    public static double logBeta(final double p, final double q) {
        if (p != SpecialMath.logBetaCache_p.get() || q != SpecialMath.logBetaCache_q.get()) {
            SpecialMath.logBetaCache_p.set(new Double(p));
            SpecialMath.logBetaCache_q.set(new Double(q));
            double res;
            if (p <= 0.0 || q <= 0.0 || p + q > 2.55E305) {
                res = 0.0;
            }
            else {
                res = logGamma(p) + logGamma(q) - logGamma(p + q);
            }
            SpecialMath.logBetaCache_res.set(new Double(res));
            return res;
        }
        return SpecialMath.logBetaCache_res.get();
    }
    
    public static double incompleteBeta(final double x, final double p, final double q) {
        if (x <= 0.0) {
            return 0.0;
        }
        if (x >= 1.0) {
            return 1.0;
        }
        if (p <= 0.0 || q <= 0.0 || p + q > 2.55E305) {
            return 0.0;
        }
        final double beta_gam = Math.exp(-logBeta(p, q) + p * Math.log(x) + q * Math.log(1.0 - x));
        if (x < (p + 1.0) / (p + q + 2.0)) {
            return beta_gam * betaFraction(x, p, q) / p;
        }
        return 1.0 - beta_gam * betaFraction(1.0 - x, q, p) / q;
    }
    
    private static double betaFraction(final double x, final double p, final double q) {
        double c = 1.0;
        final double sum_pq = p + q;
        final double p_plus = p + 1.0;
        final double p_minus = p - 1.0;
        double h = 1.0 - sum_pq * x / p_plus;
        if (Math.abs(h) < 2.23E-308) {
            h = 2.23E-308;
        }
        double frac;
        h = (frac = 1.0 / h);
        int m = 1;
        for (double delta = 0.0; m <= 1000000000 && Math.abs(delta - 1.0) > 8.88E-16; delta = h * c, frac *= delta, ++m) {
            final int m2 = 2 * m;
            double d = m * (q - m) * x / ((p_minus + m2) * (p + m2));
            h = 1.0 + d * h;
            if (Math.abs(h) < 2.23E-308) {
                h = 2.23E-308;
            }
            h = 1.0 / h;
            c = 1.0 + d / c;
            if (Math.abs(c) < 2.23E-308) {
                c = 2.23E-308;
            }
            frac *= h * c;
            d = -(p + m) * (sum_pq + m) * x / ((p + m2) * (p_plus + m2));
            h = 1.0 + d * h;
            if (Math.abs(h) < 2.23E-308) {
                h = 2.23E-308;
            }
            h = 1.0 / h;
            c = 1.0 + d / c;
            if (Math.abs(c) < 2.23E-308) {
                c = 2.23E-308;
            }
        }
        return frac;
    }
    
    public static double error(final double x) {
        final double abs_x = (x >= 0.0) ? x : (-x);
        double retval;
        if (abs_x < 0.84375) {
            if (abs_x < 3.725290298461914E-9) {
                retval = abs_x + abs_x * 0.1283791670955126;
            }
            else {
                final double s = x * x;
                final double P = SpecialMath.ePp[0] + s * (SpecialMath.ePp[1] + s * (SpecialMath.ePp[2] + s * (SpecialMath.ePp[3] + s * SpecialMath.ePp[4])));
                final double Q = 1.0 + s * (SpecialMath.eQq[0] + s * (SpecialMath.eQq[1] + s * (SpecialMath.eQq[2] + s * (SpecialMath.eQq[3] + s * SpecialMath.eQq[4]))));
                retval = abs_x + abs_x * (P / Q);
            }
        }
        else if (abs_x < 1.25) {
            final double s = abs_x - 1.0;
            final double P = SpecialMath.ePa[0] + s * (SpecialMath.ePa[1] + s * (SpecialMath.ePa[2] + s * (SpecialMath.ePa[3] + s * (SpecialMath.ePa[4] + s * (SpecialMath.ePa[5] + s * SpecialMath.ePa[6])))));
            final double Q = 1.0 + s * (SpecialMath.eQa[0] + s * (SpecialMath.eQa[1] + s * (SpecialMath.eQa[2] + s * (SpecialMath.eQa[3] + s * (SpecialMath.eQa[4] + s * SpecialMath.eQa[5])))));
            retval = 0.8450629115104675 + P / Q;
        }
        else if (abs_x >= 6.0) {
            retval = 1.0;
        }
        else {
            retval = 1.0 - complementaryError(abs_x);
        }
        return (x >= 0.0) ? retval : (-retval);
    }
    
    public static double complementaryError(final double x) {
        final double abs_x = (x >= 0.0) ? x : (-x);
        double retval;
        if (abs_x < 1.25) {
            retval = 1.0 - error(abs_x);
        }
        else if (abs_x > 28.0) {
            retval = 0.0;
        }
        else {
            final double s = 1.0 / (abs_x * abs_x);
            double R;
            double S;
            if (abs_x < 2.8571428) {
                R = SpecialMath.eRa[0] + s * (SpecialMath.eRa[1] + s * (SpecialMath.eRa[2] + s * (SpecialMath.eRa[3] + s * (SpecialMath.eRa[4] + s * (SpecialMath.eRa[5] + s * (SpecialMath.eRa[6] + s * SpecialMath.eRa[7]))))));
                S = 1.0 + s * (SpecialMath.eSa[0] + s * (SpecialMath.eSa[1] + s * (SpecialMath.eSa[2] + s * (SpecialMath.eSa[3] + s * (SpecialMath.eSa[4] + s * (SpecialMath.eSa[5] + s * (SpecialMath.eSa[6] + s * SpecialMath.eSa[7])))))));
            }
            else {
                R = SpecialMath.eRb[0] + s * (SpecialMath.eRb[1] + s * (SpecialMath.eRb[2] + s * (SpecialMath.eRb[3] + s * (SpecialMath.eRb[4] + s * (SpecialMath.eRb[5] + s * SpecialMath.eRb[6])))));
                S = 1.0 + s * (SpecialMath.eSb[0] + s * (SpecialMath.eSb[1] + s * (SpecialMath.eSb[2] + s * (SpecialMath.eSb[3] + s * (SpecialMath.eSb[4] + s * (SpecialMath.eSb[5] + s * SpecialMath.eSb[6]))))));
            }
            retval = Math.exp(-x * x - 0.5625 + R / S) / abs_x;
        }
        return (x >= 0.0) ? retval : (2.0 - retval);
    }
    
    static {
        ai0cs = new double[] { 0.07575994494023797, 0.00759138081082334, 4.1531313389237E-4, 1.070076463439E-5, -7.90117997921E-6, -7.8261435014E-7, 2.7838499429E-7, 8.2524726E-9, -1.204463945E-8, 1.55964859E-9, 2.2925563E-10, -1.1916228E-10, 1.757854E-11, 1.12822E-12, -1.14684E-12, 2.7155E-13, -2.415E-14, -6.08E-15, 3.14E-15, -7.1E-16, 7.0E-17 };
        ai02cs = new double[] { 0.05449041101410882, 0.00336911647825569, 6.889758346918E-5, 2.89137052082E-6, 2.0489185893E-7, 2.266668991E-8, 3.39623203E-9, 4.9406022E-10, 1.188914E-11, -3.149915E-11, -1.32158E-11, -1.79419E-12, 7.1801E-13, 3.8529E-13, 1.539E-14, -4.151E-14, -9.54E-15, 3.82E-15, 1.76E-15, -3.4E-16, -2.7E-16, 3.0E-17 };
        ai1cs = new double[] { -0.02846744181881479, -0.01922953231443221, -6.1151858579437E-4, -2.06997125335E-5, 8.58561914581E-6, 1.04949824671E-6, -2.9183389184E-7, -1.559378146E-8, 1.318012367E-8, -1.44842341E-9, -2.9085122E-10, 1.2663889E-10, -1.664947E-11, -1.66665E-12, 1.2426E-12, -2.7315E-13, 2.023E-14, 7.3E-15, -3.33E-15, 7.1E-16, -6.0E-17 };
        ai12cs = new double[] { 0.02857623501828014, -0.00976109749136147, -1.1058893876263E-4, -3.88256480887E-6, -2.5122362377E-7, -2.631468847E-8, -3.83538039E-9, -5.5897433E-10, -1.897495E-11, 3.252602E-11, 1.41258E-11, 2.03564E-12, -7.1985E-13, -4.0836E-13, -2.101E-14, 4.273E-14, 1.041E-14, -3.82E-15, -1.86E-15, 3.3E-16, 2.8E-16, -3.0E-17 };
        aifcs = new double[] { -0.03797135849667, 0.05919188853726364, 9.862928057727998E-4, 6.84884381907656E-6, 2.594202596219E-8, 6.176612774E-11, 1.0092454E-13, 1.2014E-16, 1.0E-19 };
        aigcs = new double[] { 0.01815236558116127, 0.02157256316601076, 2.5678356987483E-4, 1.42652141197E-6, 4.57211492E-9, 9.52517E-12, 1.392E-14, 1.0E-17 };
        aipcs = new double[] { -0.0187519297793868, -0.0091443848250055, 9.010457337825E-4, -1.394184127221E-4, 2.73815815785E-5, -6.2750421119E-6, 1.6064844184E-6, -4.476392158E-7, 1.334635874E-7, -4.20735334E-8, 1.3902199E-8, -4.7831848E-9, 1.7047897E-9, -6.268389E-10, 2.369824E-10, -9.18641E-11, 3.64278E-11, -1.47475E-11, 6.0851E-12, -2.5552E-12, 1.0906E-12, -4.725E-13, 2.076E-13, -9.24E-14, 4.17E-14, -1.9E-14, 8.7E-15, -4.0E-15, 1.9E-15, -9.0E-16, 4.0E-16, -2.0E-16, 1.0E-16, -0.0 };
        am21cs = new double[] { 0.0065809191761485, 0.0023675984685722, 1.324741670371E-4, 1.57600904043E-5, 2.7529702663E-6, 6.102679017E-7, 1.595088468E-7, 4.71033947E-8, 1.52933871E-8, 5.3590722E-9, 2.000091E-9, 7.872292E-10, 3.243103E-10, 1.390106E-10, 6.17011E-11, 2.82491E-11, 1.32979E-11, 6.4188E-12, 3.1697E-12, 1.5981E-12, 8.213E-13, 4.296E-13, 2.284E-13, 1.232E-13, 6.75E-14, 3.74E-14, 2.1E-14, 1.19E-14, 6.8E-15, 3.9E-15, 2.3E-15, 1.3E-15, 8.0E-16, 5.0E-16, 3.0E-16, 1.0E-16, 1.0E-16, 0.0, 0.0, 0.0 };
        ath1cs = new double[] { -0.07125837815669366, -0.00590471979831451, -1.2114544069499E-4, -9.8860854227E-6, -1.38084097352E-6, -2.6142640172E-7, -6.050432589E-8, -1.618436223E-8, -4.83464911E-9, -1.57655272E-9, -5.5231518E-10, -2.0545441E-10, -8.043412E-11, -3.291252E-11, -1.399875E-11, -6.16151E-12, -2.79614E-12, -1.30428E-12, -6.2373E-13, -3.0512E-13, -1.5239E-13, -7.758E-14, -4.02E-14, -2.117E-14, -1.132E-14, -6.14E-15, -3.37E-15, -1.88E-15, -1.05E-15, -6.0E-16, -3.4E-16, -2.0E-16, -1.1E-16, -7.0E-17, -4.0E-17, -2.0E-17 };
        am22cs = new double[] { -0.01562844480625341, 0.00778336445239681, 8.6705777047718E-4, 1.5696627315611E-4, 3.563962571432E-5, 9.24598335425E-6, 2.6211016185E-6, 7.9188221651E-7, 2.5104152792E-7, 8.265223206E-8, 2.805711662E-8, 9.7682109E-9, 3.47407923E-9, 1.25828132E-9, 4.6298826E-10, 1.7272825E-10, 6.523192E-11, 2.490471E-11, 9.60156E-12, 3.73448E-12, 1.46417E-12, 5.7826E-13, 2.2991E-13, 9.197E-14, 3.7E-14, 1.496E-14, 6.08E-15, 2.48E-15, 1.01E-15, 4.1E-16, 1.7E-16, 7.0E-17, 2.0E-17 };
        ath2cs = new double[] { 0.00440527345871877, -0.03042919452318455, -0.00138565328377179, -1.8044439089549E-4, -3.380847108327E-5, -7.67818353522E-6, -1.96783944371E-6, -5.4837271158E-7, -1.6254615505E-7, -5.053049981E-8, -1.631580701E-8, -5.43420411E-9, -1.85739855E-9, -6.489512E-10, -2.3105948E-10, -8.363282E-11, -3.071196E-11, -1.142367E-11, -4.29811E-12, -1.63389E-12, -6.2693E-13, -2.426E-13, -9.461E-14, -3.716E-14, -1.469E-14, -5.84E-15, -2.33E-15, -9.3E-16, -3.7E-16, -1.5E-16, -6.0E-17, -2.0E-17 };
        bi0cs = new double[] { -0.07660547252839145, 1.9273379539938083, 0.22826445869203013, 0.013048914667072904, 4.344270900816487E-4, 9.42265768600193E-6, 1.4340062895106E-7, 1.61384906966E-9, 1.396650044E-11, 9.579451E-14, 5.3339E-16, 2.45E-18 };
        bj0cs = new double[] { 0.10025416196893913, -0.6652230077644051, 0.2489837034982813, -0.03325272317003577, 0.0023114179304694017, -9.9112774199508E-5, 2.8916708643998E-6, -6.1210858663E-8, 9.838650793E-10, -1.24235515E-11, 1.265433E-13, -1.0619E-15, 7.4E-18 };
        bm0cs = new double[] { 0.09284961637381645, -0.00142987707403484, 2.830579271257E-5, -1.43300611424E-6, 1.2028628046E-7, -1.397113013E-8, 2.04076188E-9, -3.5399669E-10, 7.024759E-11, -1.554107E-11, 3.76226E-12, -9.8282E-13, 2.7408E-13, -8.091E-14, 2.511E-14, -8.14E-15, 2.75E-15, -9.6E-16, 3.4E-16, -1.2E-16, 4.0E-17 };
        bth0cs = new double[] { -0.24639163774300119, 0.001737098307508963, -6.2183633402968E-5, 4.368050165742E-6, -4.56093019869E-7, 6.2197400101E-8, -1.0300442889E-8, 1.979526776E-9, -4.28198396E-10, 1.0203584E-10, -2.6363898E-11, 7.297935E-12, -2.144188E-12, 6.63693E-13, -2.15126E-13, 7.2659E-14, -2.5465E-14, 9.229E-15, -3.448E-15, 1.325E-15, -5.22E-16, 2.1E-16, -8.7E-17, 3.6E-17 };
        by0cs = new double[] { -0.011277839392865573, -0.12834523756042035, -0.10437884799794249, 0.023662749183969694, -0.002090391647700486, 1.03975453939057E-4, -3.369747162423E-6, 7.7293842676E-8, -1.324976772E-9, 1.7648232E-11, -1.88105E-13, 1.641E-15, -1.1E-17 };
        bi1cs = new double[] { -0.001971713261099859, 0.4073488766754648, 0.03483899429995946, 0.001545394556300123, 4.1888521098377E-5, 7.64902676483E-7, 1.0042493924E-8, 9.9322077E-11, 7.6638E-13, 4.741E-15, 2.4E-17 };
        bj1cs = new double[] { -0.11726141513332787, -0.2536152183079064, 0.050127080984469566, -0.004631514809625081, 2.47996229415914E-4, -8.678948686278E-6, 2.14293917143E-7, -3.936093079E-9, 5.5911823E-11, -6.32761E-13, 5.84E-15, -4.4E-17 };
        bm1cs = new double[] { 0.1047362510931285, 0.00442443893702345, -5.661639504035E-5, 2.31349417339E-6, -1.7377182007E-7, 1.89320993E-8, -2.65416023E-9, 4.4740209E-10, -8.691795E-11, 1.891492E-11, -4.51884E-12, 1.16765E-12, -3.2265E-13, 9.45E-14, -2.913E-14, 9.39E-15, -3.15E-15, 1.09E-15, -3.9E-16, 1.4E-16, -5.0E-17 };
        bth1cs = new double[] { 0.7406014102631385, -0.00457175565963769, 1.19818510964326E-4, -6.964561891648E-6, 6.55495621447E-7, -8.4066228945E-8, 1.3376886564E-8, -2.499565654E-9, 5.294951E-10, -1.24135944E-10, 3.1656485E-11, -8.66864E-12, 2.523758E-12, -7.75085E-13, 2.49527E-13, -8.3773E-14, 2.9205E-14, -1.0534E-14, 3.919E-15, -1.5E-15, 5.89E-16, -2.37E-16, 9.7E-17, -4.0E-17 };
        by1cs = new double[] { 0.032080471006119084, 1.2627078974335004, 0.006499961899923175, -0.08936164528860505, 0.013250881221757096, -8.979059119648352E-4, 3.647361487958306E-5, -1.001374381666E-6, 1.99453965739E-8, -3.0230656018E-10, 3.60987815E-12, -3.487488E-14, 2.7838E-16, -1.86E-18 };
        LOGSQRT2PI = Math.log(2.5066282746310007);
        g_p = new double[] { -1.716185138865495, 24.76565080557592, -379.80425647094563, 629.3311553128184, 866.9662027904133, -31451.272968848367, -36144.413418691176, 66456.14382024054 };
        g_q = new double[] { -30.840230011973897, 315.35062697960416, -1015.1563674902192, -3107.771671572311, 22538.11842098015, 4755.846277527881, -134659.9598649693, -115132.25967555349 };
        g_c = new double[] { -0.001910444077728, 8.4171387781295E-4, -5.952379913043012E-4, 7.936507935003503E-4, -0.0027777777777776816, 0.08333333333333333, 0.0057083835261 };
        lg_p1 = new double[] { 4.945235359296727, 201.8112620856775, 2290.8383738313464, 11319.672059033808, 28557.246356716354, 38484.962284437934, 26377.487876241954, 7225.813979700288 };
        lg_q1 = new double[] { 67.48212550303778, 1113.3323938571993, 7738.757056935398, 27639.870744033407, 54993.102062261576, 61611.22180066002, 36351.2759150194, 8785.536302431014 };
        lg_p2 = new double[] { 4.974607845568932, 542.4138599891071, 15506.93864978365, 184793.29044456323, 1088204.7694688288, 3338152.96798703, 5106661.678927353, 3074109.0548505397 };
        lg_q2 = new double[] { 183.03283993705926, 7765.049321445006, 133190.38279660742, 1136705.8213219696, 5267964.117437947, 1.3467014543111017E7, 1.7827365303532742E7, 9533095.591844354 };
        lg_p4 = new double[] { 14745.0216605994, 2426813.3694867045, 1.2147555740450932E8, 2.663432449630977E9, 2.940378956634554E10, 1.702665737765399E11, 4.926125793377431E11, 5.606251856223951E11 };
        lg_q4 = new double[] { 2690.5301758708993, 639388.5654300093, 4.135599930241388E7, 1.120872109616148E9, 1.4886137286788137E10, 1.0168035862724382E11, 3.4174763455073773E11, 4.463158187419713E11 };
        lg_c = new double[] { -0.001910444077728, 8.4171387781295E-4, -5.952379913043012E-4, 7.936507935003503E-4, -0.0027777777777776816, 0.08333333333333333, 0.0057083835261 };
        logGammaCache_res = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                return new Double(0.0);
            }
        };
        logGammaCache_x = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                return new Double(0.0);
            }
        };
        logBetaCache_res = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                return new Double(0.0);
            }
        };
        logBetaCache_p = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                return new Double(0.0);
            }
        };
        logBetaCache_q = new ThreadLocal() {
            @Override
            protected Object initialValue() {
                return new Double(0.0);
            }
        };
        ePp = new double[] { 0.12837916709551256, -0.3250421072470015, -0.02848174957559851, -0.005770270296489442, -2.3763016656650163E-5 };
        eQq = new double[] { 0.39791722395915535, 0.0650222499887673, 0.005081306281875766, 1.3249473800432164E-4, -3.960228278775368E-6 };
        ePa = new double[] { -0.0023621185607526594, 0.41485611868374833, -0.3722078760357013, 0.31834661990116175, -0.11089469428239668, 0.035478304325618236, -0.002166375594868791 };
        eQa = new double[] { 0.10642088040084423, 0.540397917702171, 0.07182865441419627, 0.12617121980876164, 0.01363708391202905, 0.011984499846799107 };
        eRa = new double[] { -0.009864944034847148, -0.6938585727071818, -10.558626225323291, -62.375332450326006, -162.39666946257347, -184.60509290671104, -81.2874355063066, -9.814329344169145 };
        eSa = new double[] { 19.651271667439257, 137.65775414351904, 434.56587747522923, 645.3872717332679, 429.00814002756783, 108.63500554177944, 6.570249770319282, -0.0604244152148581 };
        eRb = new double[] { -0.0098649429247001, -0.799283237680523, -17.757954917754752, -160.63638485582192, -637.5664433683896, -1025.0951316110772, -483.5191916086514 };
        eSb = new double[] { 30.33806074348246, 325.7925129965739, 1536.729586084437, 3199.8582195085955, 2553.0504064331644, 474.52854120695537, -22.44095244658582 };
    }
}
