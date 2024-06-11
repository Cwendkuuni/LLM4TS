package JSci.maths;

import JSci.maths.polynomials.*;
import JSci.maths.vectors.*;
import JSci.maths.matrices.*;

public final class LinearMath extends AbstractMath
{
    private static final int EIGEN_MAX_ITERATIONS;
    
    public static AbstractDoubleVector solve(final AbstractDoubleSquareMatrix M, final AbstractDoubleVector v) {
        final int n = v.dimension();
        final double[] array = new double[n];
        final int[] pivot = new int[n + 1];
        final AbstractDoubleSquareMatrix[] lu = M.luDecompose(pivot);
        for (int i = 0; i < n; ++i) {
            double sum = v.getComponent(pivot[i]);
            for (int j = 0; j < i; ++j) {
                sum -= lu[0].getElement(i, j) * array[j];
            }
            array[i] = sum / lu[0].getElement(i, i);
        }
        for (int i = n - 1; i >= 0; --i) {
            double sum = array[i];
            for (int j = i + 1; j < n; ++j) {
                sum -= lu[1].getElement(i, j) * array[j];
            }
            array[i] = sum / lu[1].getElement(i, i);
        }
        return new DoubleVector(array);
    }
    
    private static double[] solveCholesky(final double[][] m, final double[] v) {
        final int n = v.length;
        final double[] array = new double[n];
        final AbstractDoubleSquareMatrix[] lu = new DoubleSquareMatrix(m).choleskyDecompose();
        for (int i = 0; i < n; ++i) {
            double sum = v[i];
            for (int j = 0; j < i; ++j) {
                sum -= lu[0].getElement(i, j) * array[j];
            }
            array[i] = sum / lu[0].getElement(i, i);
        }
        for (int i = n - 1; i >= 0; --i) {
            double sum = array[i];
            for (int j = i + 1; j < n; ++j) {
                sum -= lu[1].getElement(i, j) * array[j];
            }
            array[i] = sum / lu[1].getElement(i, i);
        }
        return array;
    }
    
    private static double[] solveQR(final double[][] m, final double[] v) {
        final int n = v.length;
        final double[] array = new double[n];
        final AbstractDoubleSquareMatrix[] lu = new DoubleSquareMatrix(m).qrDecompose();
        for (int i = 0; i < n; ++i) {
            double sum = 0.0;
            for (int j = 0; j < n; ++j) {
                sum += lu[0].getElement(j, i) * v[j];
            }
            array[i] = sum;
        }
        for (int i = n - 1; i >= 0; --i) {
            double sum = array[i];
            for (int j = i + 1; j < n; ++j) {
                sum -= lu[1].getElement(i, j) * array[j];
            }
            array[i] = sum / lu[1].getElement(i, i);
        }
        return array;
    }
    
    public static AbstractDoubleVector solveCG(final AbstractDoubleMatrix A, final AbstractDoubleVector b, AbstractDoubleVector x, final int recomputeResidualInterval, final int max_iter, final double tol) throws MaximumIterationsExceededException {
        if (max_iter <= 0) {
            throw new IllegalArgumentException("Number of allowed iterations must be a positive integer: " + max_iter + " <= 0.");
        }
        if (tol < 0.0) {
            throw new IllegalArgumentException("Tolerance must be positive or zero: " + tol + " < 0.");
        }
        final double tolSqr = tol * tol;
        AbstractDoubleVector d;
        AbstractDoubleVector r = d = b.subtract(A.multiply(x));
        double delta0;
        double deltaNew;
        int i;
        AbstractDoubleVector q;
        double alpha;
        boolean computeExactResidual;
        double deltaOld;
        double beta;
        for (deltaNew = (delta0 = r.scalarProduct(r)), i = 0; i < max_iter && deltaNew / delta0 > tolSqr; ++i) {
            q = A.multiply(d);
            alpha = deltaNew / d.scalarProduct(q);
            x = x.add(d.scalarMultiply(alpha));
            computeExactResidual = ((i + 1) % recomputeResidualInterval == 0);
            if (computeExactResidual) {
                r = b.subtract(A.multiply(x));
            }
            else {
                r = r.subtract(q.scalarMultiply(alpha));
            }
            deltaOld = deltaNew;
            deltaNew = r.scalarProduct(r);
            if (!computeExactResidual && deltaNew / delta0 <= tolSqr) {
                r = b.subtract(A.multiply(x));
                deltaNew = r.scalarProduct(r);
            }
            beta = deltaNew / deltaOld;
            d = r.add(d.scalarMultiply(beta));
        }
        if (i == max_iter) {
            throw new MaximumIterationsExceededException("No convergence after " + max_iter + " iterations", x);
        }
        return x;
    }
    
    public static AbstractDoubleVector solveGMRes(final AbstractDoubleMatrix A, final AbstractDoubleVector b, int max_iter, double tol) throws MaximumIterationsExceededException {
        if (max_iter <= 0) {
            throw new IllegalArgumentException("Number of allowed iterations must be a positive integer: " + max_iter + " <= 0.");
        }
        if (tol < 0.0) {
            throw new IllegalArgumentException("Tolerance must be positive or zero: " + tol + " < 0.");
        }
        final int m = A.rows();
        int j = 1;
        final double[] s = new double[m + 1];
        final double[][] cs = new double[m + 1][2];
        double[] rotmp = new double[2];
        AbstractDoubleVector x = new DoubleVector(A.rows());
        double normb = b.norm();
        AbstractDoubleVector r = b.subtract(A.multiply(x));
        double beta = r.norm();
        if (normb == 0.0) {
            normb = 1.0;
        }
        double resid;
        if ((resid = r.norm() / normb) <= tol) {
            tol = resid;
            max_iter = 0;
            throw new AssertionError((Object)"There is a bug.");
        }
        final AbstractDoubleVector[] v = new DoubleVector[m + 1];
        final double[][] H = new double[m + 1][m];
        while (j <= max_iter) {
            v[0] = r.scalarMultiply(1.0 / beta);
            for (int i = 0; i < m + 1; ++i) {
                s[i] = 0.0;
            }
            s[0] = beta;
            for (int i = 0; i < m && j <= max_iter; ++i, ++j) {
                AbstractDoubleVector w = A.multiply(v[i]);
                for (int k = 0; k <= i; ++k) {
                    H[k][i] = w.scalarProduct(v[k]);
                    w = w.subtract(v[k].scalarMultiply(H[k][i]));
                }
                H[i + 1][i] = w.norm();
                v[i + 1] = w.scalarMultiply(1.0 / H[i + 1][i]);
                for (int k = 0; k < i; ++k) {
                    rotmp = applyPlaneRotation(H[k][i], H[k + 1][i], cs[k][0], cs[k][1]);
                    H[k][i] = rotmp[0];
                    H[k + 1][i] = rotmp[1];
                }
                cs[i] = generatePlaneRotation(H[i][i], H[i + 1][i]);
                rotmp = applyPlaneRotation(H[i][i], H[i + 1][i], cs[i][0], cs[i][1]);
                H[i][i] = rotmp[0];
                H[i + 1][i] = rotmp[1];
                rotmp = applyPlaneRotation(s[i], s[i + 1], cs[i][0], cs[i][1]);
                s[i] = rotmp[0];
                s[i + 1] = rotmp[1];
                if ((resid = Math.abs(s[i + 1]) / normb) < tol) {
                    x = update(x, i, H, s, v);
                    tol = resid;
                    max_iter = j;
                    return x;
                }
            }
            x = update(x, m - 1, H, s, v);
            r = b.subtract(A.multiply(x));
            beta = r.norm();
            if ((resid = beta / normb) < tol) {
                tol = resid;
                max_iter = j;
                return x;
            }
        }
        tol = resid;
        throw new MaximumIterationsExceededException("(tol) " + tol + ". It doesn't converge in " + max_iter + "iterations. Try raising the number of allowed iterations or raising the tolerance.", x);
    }
    
    private static double[] generatePlaneRotation(final double dx, final double dy) {
        final double[] cs = new double[2];
        if (dy == 0.0) {
            cs[0] = 1.0;
            cs[1] = 0.0;
        }
        else if (Math.abs(dy) > Math.abs(dx)) {
            final double temp = dx / dy;
            cs[1] = 1.0 / Math.sqrt(1.0 + temp * temp);
            cs[0] = temp * cs[1];
        }
        else {
            final double temp = dy / dx;
            cs[0] = 1.0 / Math.sqrt(1.0 + temp * temp);
            cs[1] = temp * cs[0];
        }
        return cs;
    }
    
    private static double[] applyPlaneRotation(final double dx, final double dy, final double cs, final double sn) {
        final double[] dxy = { cs * dx + sn * dy, -sn * dx + cs * dy };
        return dxy;
    }
    
    private static AbstractDoubleVector update(AbstractDoubleVector x, final int k, final double[][] H, final double[] s, final AbstractDoubleVector[] v) {
        for (int i = k; i >= 0; --i) {
            s[i] /= H[i][i];
            for (int j = i - 1; j >= 0; --j) {
                s[j] -= H[j][i] * s[i];
            }
        }
        for (int l = 0; l <= k; ++l) {
            x = x.add(v[l].scalarMultiply(s[l]));
        }
        return x;
    }
    
    public static RealPolynomial leastSquaresFit(int n, final double[][] data) {
        final int nm1 = n++;
        final double[][] mArray = new double[n][n];
        final double[] vArray = new double[n];
        for (int i = 0; i < n; ++i) {
            double xsum;
            double xysum = xsum = 0.0;
            for (int j = 0; j < data[0].length; ++j) {
                final double tmp = Math.pow(data[0][j], i);
                xsum += tmp;
                xysum += tmp * data[1][j];
            }
            mArray[0][i] = xsum;
            vArray[i] = xysum;
        }
        for (int i = 1; i < n; ++i) {
            System.arraycopy(mArray[i - 1], 1, mArray[i], 0, nm1);
            double xsum = 0.0;
            for (int j = 0; j < data[0].length; ++j) {
                xsum += Math.pow(data[0][j], nm1 + i);
            }
            mArray[i][nm1] = xsum;
        }
        return new RealPolynomial(solveCholesky(mArray, vArray));
    }
    
    public static AbstractDoubleVector linearRegression(final double[][] data) {
        final int y = data.length - 1;
        final double[][] mArray = new double[data.length][data.length];
        final double[] vArray = new double[data.length];
        mArray[0][0] = data[0].length;
        for (int j = 1; j < data.length; ++j) {
            double xsum = 0.0;
            for (int k = 0; k < data[0].length; ++k) {
                xsum += data[j - 1][k];
            }
            mArray[0][j] = (mArray[j][0] = xsum);
        }
        double xysum = 0.0;
        for (int k = 0; k < data[0].length; ++k) {
            xysum += data[y][k];
        }
        vArray[0] = xysum;
        for (int i = 1; i < data.length; ++i) {
            for (int j = i; j < data.length; ++j) {
                double xsum = 0.0;
                for (int k = 0; k < data[0].length; ++k) {
                    xsum += data[i - 1][k] * data[j - 1][k];
                }
                mArray[i][j] = (mArray[j][i] = xsum);
            }
            xysum = 0.0;
            for (int k = 0; k < data[0].length; ++k) {
                xysum += data[i - 1][k] * data[y][k];
            }
            vArray[i] = xysum;
        }
        return new DoubleVector(solveCholesky(mArray, vArray));
    }
    
    public static AbstractDoubleVector[] orthonormalize(final AbstractDoubleVector[] vecs) {
        final int N = vecs.length;
        final AbstractDoubleVector[] orthovecs = new DoubleVector[N];
        for (int i = 0; i < N; ++i) {
            orthovecs[i] = vecs[i];
            for (int j = 0; j < i; ++j) {
                orthovecs[i] = orthovecs[i].subtract(orthovecs[j].scalarMultiply(orthovecs[j].scalarProduct(orthovecs[i])));
            }
            orthovecs[i] = orthovecs[i].normalize();
        }
        return orthovecs;
    }
    
    public static double[] eigenvalueSolveHermitian(final AbstractComplexSquareMatrix matrix) throws MaximumIterationsExceededException {
        final int n = matrix.rows();
        final double[][] matrix2 = new double[2 * n][2 * n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                final double real = matrix.getRealElement(i, j);
                final double imag = matrix.getImagElement(i, j);
                matrix2[i][j] = real;
                matrix2[n + i][n + j] = real;
                matrix2[n + i][j] = imag;
                matrix2[i][n + j] = -imag;
            }
        }
        final double[] eigenvalue2 = new double[2 * n];
        final double[] offdiag = new double[2 * n];
        reduceSymmetric1_SquareToTridiagonal(matrix2, eigenvalue2, offdiag);
        System.arraycopy(offdiag, 1, offdiag, 0, n - 1);
        offdiag[n - 1] = 0.0;
        eigenvalueSolveSymmetricTridiagonalMatrix(eigenvalue2, offdiag);
        final double[] eigenvalue3 = new double[n];
        System.arraycopy(eigenvalue2, 0, eigenvalue3, 0, n);
        return eigenvalue3;
    }
    
    public static double[] eigenSolveHermitian(final AbstractComplexSquareMatrix matrix, final AbstractComplexVector[] eigenvector) throws MaximumIterationsExceededException {
        final int n = matrix.rows();
        final double[][] matrix2 = new double[2 * n][2 * n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                final double real = matrix.getRealElement(i, j);
                final double imag = matrix.getImagElement(i, j);
                matrix2[i][j] = real;
                matrix2[n + i][n + j] = real;
                matrix2[n + i][j] = imag;
                matrix2[i][n + j] = -imag;
            }
        }
        final double[] eigenvalue2 = new double[2 * n];
        final double[] offdiag = new double[2 * n];
        reduceSymmetric2_SquareToTridiagonal(matrix2, eigenvalue2, offdiag);
        System.arraycopy(offdiag, 1, offdiag, 0, n - 1);
        offdiag[n - 1] = 0.0;
        eigenSolveSymmetricTridiagonalMatrix(eigenvalue2, offdiag, matrix2);
        final double[] eigenvalue3 = new double[n];
        for (int i = 0; i < n; ++i) {
            eigenvalue3[i] = eigenvalue2[i];
            final double[] arrayRe = new double[n];
            final double[] arrayIm = new double[n];
            for (int j = 0; j < n; ++j) {
                arrayRe[j] = matrix2[j][i];
                arrayIm[j] = matrix2[j + n][i];
            }
            eigenvector[i] = new ComplexVector(arrayRe, arrayIm);
        }
        return eigenvalue3;
    }
    
    public static double[] eigenvalueSolveSymmetric(final DoubleTridiagonalMatrix matrix) throws MaximumIterationsExceededException {
        final int n = matrix.rows();
        final int nm1 = n - 1;
        final double[] eigenvalue = new double[n];
        final double[] offdiag = new double[n];
        for (int i = 0; i < nm1; ++i) {
            eigenvalue[i] = matrix.getElement(i, i);
            offdiag[i] = matrix.getElement(i, i + 1);
        }
        eigenvalue[nm1] = matrix.getElement(nm1, nm1);
        offdiag[nm1] = 0.0;
        eigenvalueSolveSymmetricTridiagonalMatrix(eigenvalue, offdiag);
        return eigenvalue;
    }
    
    public static double[] eigenSolveSymmetric(final DoubleTridiagonalMatrix matrix, final AbstractDoubleVector[] eigenvector) throws MaximumIterationsExceededException {
        final int n = matrix.rows();
        final int nm1 = n - 1;
        final double[] eigenvalue = new double[n];
        final double[] offdiag = new double[n];
        final double[][] id = new double[n][n];
        for (int i = 0; i < nm1; ++i) {
            id[i][i] = 1.0;
            eigenvalue[i] = matrix.getElement(i, i);
            offdiag[i] = matrix.getElement(i, i + 1);
        }
        id[nm1][nm1] = 1.0;
        eigenvalue[nm1] = matrix.getElement(nm1, nm1);
        offdiag[nm1] = 0.0;
        eigenSolveSymmetricTridiagonalMatrix(eigenvalue, offdiag, id);
        for (int i = 0; i < n; ++i) {
            final DoubleVector evec = new DoubleVector(n);
            for (int j = 0; j < n; ++j) {
                evec.setComponent(j, id[j][i]);
            }
            eigenvector[i] = evec;
        }
        return eigenvalue;
    }
    
    public static double[] eigenvalueSolveSymmetric(final AbstractDoubleSquareMatrix matrix) throws MaximumIterationsExceededException {
        final int n = matrix.rows();
        final double[] eigenvalue = new double[n];
        final double[] offdiag = new double[n];
        final double[][] array = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                array[i][j] = matrix.getElement(i, j);
            }
        }
        reduceSymmetric1_SquareToTridiagonal(array, eigenvalue, offdiag);
        System.arraycopy(offdiag, 1, offdiag, 0, n - 1);
        offdiag[n - 1] = 0.0;
        eigenvalueSolveSymmetricTridiagonalMatrix(eigenvalue, offdiag);
        return eigenvalue;
    }
    
    public static double[] eigenSolveSymmetric(final AbstractDoubleSquareMatrix matrix, final AbstractDoubleVector[] eigenvector) throws MaximumIterationsExceededException {
        final int n = matrix.rows();
        final double[] eigenvalue = new double[n];
        final double[] offdiag = new double[n];
        final double[][] transf = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                transf[i][j] = matrix.getElement(i, j);
            }
        }
        reduceSymmetric2_SquareToTridiagonal(transf, eigenvalue, offdiag);
        System.arraycopy(offdiag, 1, offdiag, 0, n - 1);
        offdiag[n - 1] = 0.0;
        eigenSolveSymmetricTridiagonalMatrix(eigenvalue, offdiag, transf);
        for (int i = 0; i < n; ++i) {
            final DoubleVector evec = new DoubleVector(n);
            for (int j = 0; j < n; ++j) {
                evec.setComponent(j, transf[j][i]);
            }
            eigenvector[i] = evec;
        }
        return eigenvalue;
    }
    
    public static void diagonalizeSymmetric(final AbstractDoubleSquareMatrix matrix) throws MaximumIterationsExceededException {
        final int n = matrix.rows();
        final double[] offdiag = new double[n];
        reduceSymmetric1_SquareToTridiagonal(matrix, offdiag);
        System.arraycopy(offdiag, 1, offdiag, 0, n - 1);
        offdiag[n - 1] = 0.0;
        eigenvalueSolveSymmetricTridiagonalMatrix(matrix, offdiag);
    }
    
    private static void eigenvalueSolveSymmetricTridiagonalMatrix(final double[] diag, final double[] offdiag) throws MaximumIterationsExceededException {
        final int n = diag.length;
        final int nm1 = n - 1;
        for (int l = 0; l < n; ++l) {
            int iteration = 0;
            int m;
            do {
                for (m = l; m < nm1; ++m) {
                    final double dd = Math.abs(diag[m]) + Math.abs(diag[m + 1]);
                    if (Math.abs(offdiag[m]) + dd == dd) {
                        break;
                    }
                }
                if (m != l) {
                    if (iteration++ == LinearMath.EIGEN_MAX_ITERATIONS) {
                        throw new MaximumIterationsExceededException("No convergence after " + LinearMath.EIGEN_MAX_ITERATIONS + " iterations.", diag);
                    }
                    double g = (diag[l + 1] - diag[l]) / (2.0 * offdiag[l]);
                    double r = Math.sqrt(g * g + 1.0);
                    g = diag[m] - diag[l] + offdiag[l] / (g + ((g < 0.0) ? (-Math.abs(r)) : Math.abs(r)));
                    double s;
                    double c = s = 1.0;
                    double p = 0.0;
                    for (int i = m - 1; i >= l; --i) {
                        final double f = s * offdiag[i];
                        final double b = c * offdiag[i];
                        if (Math.abs(f) >= Math.abs(g)) {
                            c = g / f;
                            r = Math.sqrt(c * c + 1.0);
                            offdiag[i + 1] = f * r;
                            s = 1.0 / r;
                            c *= s;
                        }
                        else {
                            s = f / g;
                            r = Math.sqrt(s * s + 1.0);
                            offdiag[i + 1] = g * r;
                            c = 1.0 / r;
                            s *= c;
                        }
                        g = diag[i + 1] - p;
                        r = (diag[i] - g) * s + 2.0 * c * b;
                        p = s * r;
                        diag[i + 1] = g + p;
                        g = c * r - b;
                    }
                    diag[l] -= p;
                    offdiag[l] = g;
                    offdiag[m] = 0.0;
                }
            } while (m != l);
        }
    }
    
    private static void eigenvalueSolveSymmetricTridiagonalMatrix(final AbstractDoubleSquareMatrix matrix, final double[] offdiag) throws MaximumIterationsExceededException {
        final int n = matrix.rows();
        final int nm1 = n - 1;
        for (int l = 0; l < n; ++l) {
            int iteration = 0;
            int m;
            do {
                for (m = l; m < nm1; ++m) {
                    final double dd = Math.abs(matrix.getElement(m, m)) + Math.abs(matrix.getElement(m + 1, m + 1));
                    if (Math.abs(offdiag[m]) + dd == dd) {
                        break;
                    }
                }
                if (m != l) {
                    if (iteration++ == LinearMath.EIGEN_MAX_ITERATIONS) {
                        throw new MaximumIterationsExceededException("No convergence after " + LinearMath.EIGEN_MAX_ITERATIONS + " iterations.", matrix);
                    }
                    double g = (matrix.getElement(l + 1, l + 1) - matrix.getElement(l, l)) / (2.0 * offdiag[l]);
                    double r = Math.sqrt(g * g + 1.0);
                    g = matrix.getElement(m, m) - matrix.getElement(l, l) + offdiag[l] / (g + ((g < 0.0) ? (-Math.abs(r)) : Math.abs(r)));
                    double s;
                    double c = s = 1.0;
                    double p = 0.0;
                    for (int i = m - 1; i >= l; --i) {
                        final double f = s * offdiag[i];
                        final double b = c * offdiag[i];
                        if (Math.abs(f) >= Math.abs(g)) {
                            c = g / f;
                            r = Math.sqrt(c * c + 1.0);
                            offdiag[i + 1] = f * r;
                            s = 1.0 / r;
                            c *= s;
                        }
                        else {
                            s = f / g;
                            r = Math.sqrt(s * s + 1.0);
                            offdiag[i + 1] = g * r;
                            c = 1.0 / r;
                            s *= c;
                        }
                        g = matrix.getElement(i + 1, i + 1) - p;
                        r = (matrix.getElement(i, i) - g) * s + 2.0 * c * b;
                        p = s * r;
                        matrix.setElement(i + 1, i + 1, g + p);
                        g = c * r - b;
                    }
                    matrix.setElement(l, l, matrix.getElement(l, l) - p);
                    offdiag[l] = g;
                    offdiag[m] = 0.0;
                }
            } while (m != l);
        }
    }
    
    private static void reduceSymmetric1_SquareToTridiagonal(final double[][] matrix, final double[] diag, final double[] offdiag) {
        final int n = diag.length;
        for (int i = n - 1; i > 0; --i) {
            final int l = i - 1;
            double h;
            double scale = h = 0.0;
            if (l > 0) {
                for (int k = 0; k <= l; ++k) {
                    scale += Math.abs(matrix[i][k]);
                }
                if (scale == 0.0) {
                    offdiag[i] = matrix[i][l];
                }
                else {
                    for (int k = 0; k <= l; ++k) {
                        final double tmp = matrix[i][k] / scale;
                        matrix[i][k] = tmp;
                        h += tmp * tmp;
                    }
                    double f = matrix[i][l];
                    double g = (f >= 0.0) ? (-Math.sqrt(h)) : Math.sqrt(h);
                    offdiag[i] = scale * g;
                    h -= f * g;
                    matrix[i][l] = f - g;
                    f = 0.0;
                    for (int j = 0; j <= l; ++j) {
                        g = 0.0;
                        for (int m = 0; m <= j; ++m) {
                            g += matrix[j][m] * matrix[i][m];
                        }
                        for (int m = j + 1; m <= l; ++m) {
                            g += matrix[m][j] * matrix[i][m];
                        }
                        offdiag[j] = g / h;
                        f += offdiag[j] * matrix[i][j];
                    }
                    final double hh = f / (h + h);
                    for (int j2 = 0; j2 <= l; ++j2) {
                        f = matrix[i][j2];
                        g = (offdiag[j2] -= hh * f);
                        for (int k2 = 0; k2 <= j2; ++k2) {
                            final double[] array = matrix[j2];
                            final int n2 = k2;
                            array[n2] -= f * offdiag[k2] + g * matrix[i][k2];
                        }
                    }
                }
            }
            else {
                offdiag[i] = matrix[i][l];
            }
        }
        offdiag[0] = 0.0;
        for (int i = 0; i < n; ++i) {
            diag[i] = matrix[i][i];
        }
    }
    
    private static void reduceSymmetric1_SquareToTridiagonal(final AbstractDoubleSquareMatrix matrix, final double[] offdiag) {
        final int n = matrix.rows();
        for (int i = n - 1; i > 0; --i) {
            final int l = i - 1;
            double h;
            double scale = h = 0.0;
            if (l > 0) {
                for (int k = 0; k <= l; ++k) {
                    scale += Math.abs(matrix.getElement(i, k));
                }
                if (scale == 0.0) {
                    offdiag[i] = matrix.getElement(i, l);
                }
                else {
                    for (int k = 0; k <= l; ++k) {
                        final double tmp = matrix.getElement(i, k) / scale;
                        matrix.setElement(i, k, tmp);
                        h += tmp * tmp;
                    }
                    double f = matrix.getElement(i, l);
                    double g = (f >= 0.0) ? (-Math.sqrt(h)) : Math.sqrt(h);
                    offdiag[i] = scale * g;
                    h -= f * g;
                    matrix.setElement(i, l, f - g);
                    f = 0.0;
                    for (int j = 0; j <= l; ++j) {
                        g = 0.0;
                        for (int m = 0; m <= j; ++m) {
                            g += matrix.getElement(j, m) * matrix.getElement(i, m);
                        }
                        for (int m = j + 1; m <= l; ++m) {
                            g += matrix.getElement(m, j) * matrix.getElement(i, m);
                        }
                        offdiag[j] = g / h;
                        f += offdiag[j] * matrix.getElement(i, j);
                    }
                    final double hh = f / (h + h);
                    for (int j2 = 0; j2 <= l; ++j2) {
                        f = matrix.getElement(i, j2);
                        g = (offdiag[j2] -= hh * f);
                        for (int k2 = 0; k2 <= j2; ++k2) {
                            matrix.setElement(j2, k2, matrix.getElement(j2, k2) - f * offdiag[k2] - g * matrix.getElement(i, k2));
                        }
                    }
                }
            }
            else {
                offdiag[i] = matrix.getElement(i, l);
            }
        }
        offdiag[0] = 0.0;
    }
    
    private static void eigenSolveSymmetricTridiagonalMatrix(final double[] diag, final double[] offdiag, final double[][] transf) throws MaximumIterationsExceededException {
        final int n = diag.length;
        final int nm1 = n - 1;
        for (int l = 0; l < n; ++l) {
            int iteration = 0;
            int m;
            do {
                for (m = l; m < nm1; ++m) {
                    final double dd = Math.abs(diag[m]) + Math.abs(diag[m + 1]);
                    if (Math.abs(offdiag[m]) + dd == dd) {
                        break;
                    }
                }
                if (m != l) {
                    if (iteration++ == LinearMath.EIGEN_MAX_ITERATIONS) {
                        throw new MaximumIterationsExceededException("No convergence after " + LinearMath.EIGEN_MAX_ITERATIONS + " iterations.", new Object[] { diag, transf });
                    }
                    double g = (diag[l + 1] - diag[l]) / (2.0 * offdiag[l]);
                    double r = Math.sqrt(g * g + 1.0);
                    g = diag[m] - diag[l] + offdiag[l] / (g + ((g < 0.0) ? (-Math.abs(r)) : Math.abs(r)));
                    double s;
                    double c = s = 1.0;
                    double p = 0.0;
                    for (int i = m - 1; i >= l; --i) {
                        double f = s * offdiag[i];
                        final double b = c * offdiag[i];
                        if (Math.abs(f) >= Math.abs(g)) {
                            c = g / f;
                            r = Math.sqrt(c * c + 1.0);
                            offdiag[i + 1] = f * r;
                            s = 1.0 / r;
                            c *= s;
                        }
                        else {
                            s = f / g;
                            r = Math.sqrt(s * s + 1.0);
                            offdiag[i + 1] = g * r;
                            c = 1.0 / r;
                            s *= c;
                        }
                        g = diag[i + 1] - p;
                        r = (diag[i] - g) * s + 2.0 * c * b;
                        p = s * r;
                        diag[i + 1] = g + p;
                        g = c * r - b;
                        for (int k = 0; k < n; ++k) {
                            f = transf[k][i + 1];
                            transf[k][i + 1] = s * transf[k][i] + c * f;
                            transf[k][i] = c * transf[k][i] - s * f;
                        }
                    }
                    diag[l] -= p;
                    offdiag[l] = g;
                    offdiag[m] = 0.0;
                }
            } while (m != l);
        }
    }
    
    private static void reduceSymmetric2_SquareToTridiagonal(final double[][] matrix, final double[] diag, final double[] offdiag) {
        final int n = diag.length;
        for (int i = n - 1; i > 0; --i) {
            final int l = i - 1;
            double h;
            double scale = h = 0.0;
            if (l > 0) {
                for (int k = 0; k <= l; ++k) {
                    scale += Math.abs(matrix[i][k]);
                }
                if (scale == 0.0) {
                    offdiag[i] = matrix[i][l];
                }
                else {
                    for (int k = 0; k <= l; ++k) {
                        final double tmp = matrix[i][k] / scale;
                        matrix[i][k] = tmp;
                        h += tmp * tmp;
                    }
                    double f = matrix[i][l];
                    double g = (f >= 0.0) ? (-Math.sqrt(h)) : Math.sqrt(h);
                    offdiag[i] = scale * g;
                    h -= f * g;
                    matrix[i][l] = f - g;
                    f = 0.0;
                    for (int j = 0; j <= l; ++j) {
                        matrix[j][i] = matrix[i][j] / h;
                        g = 0.0;
                        for (int m = 0; m <= j; ++m) {
                            g += matrix[j][m] * matrix[i][m];
                        }
                        for (int m = j + 1; m <= l; ++m) {
                            g += matrix[m][j] * matrix[i][m];
                        }
                        offdiag[j] = g / h;
                        f += offdiag[j] * matrix[i][j];
                    }
                    final double hh = f / (h + h);
                    for (int j2 = 0; j2 <= l; ++j2) {
                        f = matrix[i][j2];
                        g = (offdiag[j2] -= hh * f);
                        for (int k2 = 0; k2 <= j2; ++k2) {
                            final double[] array = matrix[j2];
                            final int n2 = k2;
                            array[n2] -= f * offdiag[k2] + g * matrix[i][k2];
                        }
                    }
                }
            }
            else {
                offdiag[i] = matrix[i][l];
            }
            diag[i] = h;
        }
        diag[0] = (offdiag[0] = 0.0);
        for (int i = 0; i < n; ++i) {
            final int l = i - 1;
            if (diag[i] != 0.0) {
                for (int j = 0; j <= l; ++j) {
                    double g = 0.0;
                    for (int m = 0; m <= l; ++m) {
                        g += matrix[i][m] * matrix[m][j];
                    }
                    for (int m = 0; m <= l; ++m) {
                        final double[] array2 = matrix[m];
                        final int n3 = j;
                        array2[n3] -= g * matrix[m][i];
                    }
                }
            }
            diag[i] = matrix[i][i];
            matrix[i][i] = 1.0;
            for (int j = 0; j <= l; ++j) {
                matrix[j][i] = (matrix[i][j] = 0.0);
            }
        }
    }
    
    public static double[] eigenvalueSolveSymmetric(final AbstractDoubleSquareMatrix matrix, final AbstractDoubleVector initial, final int iters) throws MaximumIterationsExceededException {
        final AbstractDoubleVector[] q = new AbstractDoubleVector[iters];
        q[0] = initial;
        final double[] a = new double[iters];
        final double[] b = new double[iters];
        for (int i = 0; i < iters; ++i) {
            AbstractDoubleVector r = matrix.multiply(q[i]);
            if (i > 0) {
                r = r.subtract(q[i - 1].scalarMultiply(b[i - 1]));
            }
            a[i] = r.scalarProduct(q[i]);
            r = r.subtract(q[i].scalarMultiply(a[i]));
            r = gramSchmidtReorthogonalize(r, q, i);
            b[i] = r.norm();
            if (i < iters - 1) {
                q[i + 1] = r.scalarDivide(b[i]);
            }
        }
        eigenvalueSolveSymmetricTridiagonalMatrix(a, b);
        return a;
    }
    
    private static AbstractDoubleVector gramSchmidtReorthogonalize(AbstractDoubleVector r, final AbstractDoubleVector[] q, final int n) {
        for (int i = 0; i < n; ++i) {
            r = r.subtract(q[i].scalarMultiply(q[i].scalarProduct(r)));
        }
        return r;
    }
    
    static {
        EIGEN_MAX_ITERATIONS = Integer.getInteger("JSci.maths.eigen.iters", 250);
    }
}
