package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.BiDiagonalTransformer;
import org.apache.commons.math3.special.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        final double[][] data = {{-1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}, {1.0, -1.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();
        Assert.assertArrayEquals(new double[][]{
                {0.5745966692385373, 0.5384606324732272, 0.1732050807568877},
                {0.5384606324732272, 0.8164965809277261, -0.4082854547324513},
                {0.1732050807568877, -0.4082854547324513, 0.4082854547324513}},
                u.getData(), new double[][]{{0.5745966692385373, 0.5384606324732272, 0.1732050807568877},
                        {0.5384606324732272, 0.8164965809277261, -0.4082854547324513},
                        {0.1732050807568877, -0.4082854547324513, 0.4082854547324513}});
        Assert.assertEquals(new double[]{1.0, 1.0, 1.0}, s.getDiagonalElements(), 0.0001);
        Assert.assertArrayEquals(new double[][]{
                {-0.5384606324732272, 0.5384606324732272, -0.1732050807568877},
                {-0.8164965809277261, -0.4082854547324513, 0.4082854547324513},
                {0.4082854547324513, 0.4082854547324513, 0.4082854547324513}},
                v.getData(), new double[][]{{-0.5384606324732272, -0.8164965809277261, 0.4082854547324513},
                        {0.5384606324732272, -0.4082854547324513, 0.4082854547324513},
                        {-0.1732050807568877, 0.4082854547324513, 0.4082854547324513}});
        Assert.assertEquals(new double[]{1.0, 1.0, 1.0}, ut.getDiagonalElements(), 0.0001);
        Assert.assertEquals(new double[]{1.0, 1.0, 1.0}, vt.getDiagonalElements(), 0.0001);
        RealMatrix inv = svd.getInverse();
        RealMatrix a = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}, {3.0, 4.0}});
        RealVector b = new Array2DRowRealVector(new double[]{3.0, 5.0});
        RealVector x = inv.solve(b);
        RealMatrix result = a.multiply(x);
        RealVector expected = new Array2DRowRealVector(new double[]{2.0, 1.0});
        Assert.assertArrayEquals(expected.toArray(), x.toArray(), 0.0001);
        Assert.assertArrayEquals(new double[]{3.0, 5.0}, result.getColumnVector(0).toArray(), 0.0001);
        Assert.assertEquals(true, svd.isNonSingular());
        RealMatrix c = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0}, {0.0, 0.0}});
        RealMatrix d = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0}, {1.0, 1.0}});
        RealMatrix e = svd.getVT().multiply(d).multiply(svd.getCovariance(1.0E-15)).multiply(svd.getV());
        RealMatrix f = c.multiply(svd.getCovariance(1.0E-15)).multiply(c.transpose());
        RealMatrix g = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0}, {0.0, 1.0}});
        RealMatrix h = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0}, {1.0, 1.0}});
        RealMatrix i = h.transpose().multiply(svd.getCovariance(1.0E-15)).multiply(h);
        RealMatrix j = g.multiply(svd.getCovariance(1.0E-15)).multiply(g.transpose());
        RealMatrix k = c.transpose().multiply(f).multiply(c);
        RealMatrix l = g.transpose().multiply(i).multiply(g);
        RealMatrix m = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0}, {0.0, 1.0}});
        RealMatrix n = m.multiply(k).multiply(m.transpose());
        RealMatrix o = m.multiply(l).multiply(m.transpose());
        RealMatrix p = new Array2DRowRealMatrix(new double[][]{
                {2.0, 2.0}, {2.0, 2.0}});
        RealMatrix q = p.multiply(n).multiply(p.transpose());
        RealMatrix r = p.multiply(o).multiply(p.transpose());
        RealMatrix s = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0}, {0.0, 1.0}});
        RealMatrix t = s.multiply(e).multiply(s.transpose());
        RealMatrix u = s.multiply(i).multiply(s.transpose());
        RealMatrix v = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0}, {0.0, 1.0}});
        RealMatrix w = v.multiply(f).multiply(v.transpose());
        RealMatrix x1 = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0}, {0.0, 1.0}});
        RealMatrix y1 = x1.multiply(k).multiply(x1.transpose());
        RealMatrix z1 = x1.multiply(l).multiply(x1.transpose());
        RealMatrix x2 = new Array2DRowRealMatrix(new double[][]{
                {0.0, 1.0}, {1.0, 0.0}});
        RealMatrix y2 = x2.multiply(k).multiply(x2.transpose());
        RealMatrix z2 = x2.multiply(l).multiply(x2.transpose());
        RealMatrix a1 = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0}, {1.0, 1.0}});
        RealMatrix b1 = svd.getV();
        RealMatrix c1 = b1.multiply(a1);
        RealMatrix d1 = c1.multiply(svd.getCovariance(1.0E-15));
        RealMatrix e1 = d1.multiply(c1.transpose());
        RealMatrix f1 = new Array2DRowRealMatrix(new double[][]{
                {2.0, 2.0}, {2.0, 2.0}});
        RealMatrix g1 = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0}, {0.0, 1.0}});
        RealMatrix h1 = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0}, {0.0, 1.0}});
        Assert.assertEquals(t.multiply(q).add(r.multiply(r)).add(u).multiply(w), e);
        Assert.assertEquals(z1.add(y1).subtract(n), t);
        Assert.assertEquals(z2.add(y2).subtract(i), w);
        Assert.assertEquals(e1.multiply(e), q);
        Assert.assertEquals(k.multiply(n).multiply(k), q);
        Assert.assertEquals(l.multiply(i).multiply(l), w);
        Assert.assertEquals(o.multiply(o), w);
        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix.transpose());
        RealMatrix u2 = svd2.getU();
        RealMatrix s2 = svd2.getS();
        RealMatrix v2 = svd2.getV();
        RealMatrix ut2 = svd2.getUT();
        RealMatrix vt2 = svd2.getVT();
        Assert.assertEquals(new double[][]{
                        {0.5745966692385373, 0.5384606324732272, 0.1732050807568877},
                        {0.5384606324732272, 0.8164965809277261, -0.4082854547324513},
                        {0.1732050807568877, -0.4082854547324513, 0.4082854547324513}},
                        u2.getData(), new double[][]{{0.5745966692385373, 0.5384606324732272, 0.1732050807568877},
                                {0.5384606324732272, 0.8164965809277261, -0.4082854547324513},
                                {0.1732050807568877, -0.4082854547324513, 0.4082854547324513}});
        Assert.assertEquals(new double[]{1.0, 1.0, 1.0}, s2.getDiagonalElements(), 0.0001);
        Assert.assertArrayEquals(new double[][]{
                        {-0.5384606324732272, -0.8164965809277261, 0.4082854547324513},
                        {-0.8164965809277261, -0.4082854547324513, 0.4082854547324513},
                        {0.4082854547324513, 0.4082854547324513, 0.4082854547324513}},
                        v2.getData(), new double[][]{{-0.5384606324732272, -0.8164965809277261, 0.4082854547324513},
                                {-0.8164965809277261, -0.4082854547324513, 0.4082854547324513},
                                {0.4082854547324513, 0.4082854547324513, 0.4082854547324513}});
        Assert.assertEquals(new double[]{1.0, 1.0, 1.0}, ut2.getDiagonalElements(), 0.0001);
        Assert.assertEquals(new double[]{1.0, 1.0, 1.0}, vt2.getDiagonalElements(), 0.0001);
    }
}