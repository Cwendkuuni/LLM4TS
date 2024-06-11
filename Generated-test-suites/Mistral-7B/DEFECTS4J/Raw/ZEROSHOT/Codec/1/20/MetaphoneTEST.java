package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Metaphone class.
 */
public class MetaphoneTest {

    private Metaphone metaphone = new Metaphone();

    @Test
    public void testEmptyString() {
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testSingleCharacter() {
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));
        // test special case X
        assertEquals("S", metaphone.metaphone("X"));
    }

    @Test
    public void testInitialDoubleCharacters() {
        assertEquals("KN", metaphone.metaphone("Kn"));
        assertEquals("AE", metaphone.metaphone("Ae"));
        assertEquals("WR", metaphone.metaphone("Wr"));
    }

    @Test
    public void testInitialDoubleVowels() {
        assertEquals("AE", metaphone.metaphone("AA"));
        assertEquals("AI", metaphone.metaphone("EA"));
        assertEquals("OE", metaphone.metaphone("EO"));
        assertEquals("AU", metaphone.metaphone("AAU"));
        assertEquals("OU", metaphone.metaphone("UO"));
    }

    @Test
    public void testInitialW() {
        assertEquals("W", metaphone.metaphone("W"));
        assertEquals("WH", metaphone.metaphone("Wh"));
        assertEquals("WR", metaphone.metaphone("Wr"));
    }

    @Test
    public void testDoubleCharacters() {
        assertEquals("BA", metaphone.metaphone("Ab"));
        assertEquals("BI", metaphone.metaphone("Eb"));
        assertEquals("CH", metaphone.metaphone("Ch"));
        assertEquals("DA", metaphone.metaphone("Ad"));
        assertEquals("DJ", metaphone.metaphone("Dg"));
        assertEquals("DK", metaphone.metaphone("Dk"));
        assertEquals("DU", metaphone.metaphone("Au"));
        assertEquals("FV", metaphone.metaphone("Vf"));
        assertEquals("GJ", metaphone.metaphone("Gj"));
        assertEquals("GK", metaphone.metaphone("Gk"));
        assertEquals("HN", metaphone.metaphone("On"));
        assertEquals("HT", metaphone.metaphone("Th"));
        assertEquals("JF", metaphone.metaphone("Fj"));
        assertEquals("KN", metaphone.metaphone("Kn"));
        assertEquals("KN", metaphone.metaphone("Nk"));
        assertEquals("KN", metaphone.metaphone("Nn"));
        assertEquals("KN", metaphone.metaphone("Nr"));
        assertEquals("KN", metaphone.metaphone("Nt"));
        assertEquals("KN", metaphone.metaphone("Ny"));
        assertEquals("KN", metaphone.metaphone("Nz"));
        assertEquals("LC", metaphone.metaphone("Cl"));
        assertEquals("LG", metaphone.metaphone("Gl"));
        assertEquals("LL", metaphone.metaphone("Ll"));
        assertEquals("LP", metaphone.metaphone("Pl"));
        assertEquals("LR", metaphone.metaphone("Rl"));
        assertEquals("LR", metaphone.metaphone("Rr"));
        assertEquals("LR", metaphone.metaphone("Rt"));
        assertEquals("LR", metaphone.metaphone("Ry"));
        assertEquals("LR", metaphone.metaphone("Rz"));
        assertEquals("LP", metaphone.metaphone("Pf"));
        assertEquals("LQ", metaphone.metaphone("Qp"));
        assertEquals("LR", metaphone.metaphone("Rp"));
        assertEquals("LS", metaphone.metaphone("Ps"));
        assertEquals("LS", metaphone.metaphone("Ps"));
        assertEquals("LX", metaphone.metaphone("Xp"));
        assertEquals("LZ", metaphone.metaphone("Zp"));
        assertEquals("MB", metaphone.metaphone("Mb"));
        assertEquals("ME", metaphone.metaphone("Me"));
        assertEquals("MI", metaphone.metaphone("Mi"));
        assertEquals("MN", metaphone.metaphone("Mn"));
        assertEquals("MO", metaphone.metaphone("Mo"));
        assertEquals("MU", metaphone.metaphone("Mm"));
        assertEquals("MT", metaphone.metaphone("Mt"));
        assertEquals("MY", metaphone.metaphone("My"));
        assertEquals("MZ", metaphone.metaphone("Mz"));
        assertEquals("NB", metaphone.metaphone("Nb"));
        assertEquals("NI", metaphone.metaphone("Ni"));
        assertEquals("NK", metaphone.metaphone("Nk"));
        assertEquals("NR", metaphone.metaphone("Rn"));
        assertEquals("NT", metaphone.metaphone("Tn"));
        assertEquals("NX", metaphone.metaphone("Xn"));
        assertEquals("NZ", metaphone.metaphone("Zn"));
        assertEquals("PC", metaphone.metaphone("Cp"));
        assertEquals("PG", metaphone.metaphone("Gp"));
        assertEquals("PL", metaphone.metaphone("Lp"));
        assertEquals("PR", metaphone.metaphone("Rp"));
        assertEquals("PS", metaphone.metaphone("Sp"));
        assertEquals("PT", metaphone.metaphone("Tp"));
        assertEquals("PY", metaphone.metaphone("Yp"));
        assertEquals("PX", metaphone.metaphone("Xp"));
        assertEquals("PZ", metaphone.metaphone("Zp"));
        assertEquals("RB", metaphone.metaphone("Rb"));
        assertEquals("RI", metaphone.metaphone("Ri"));
        assertEquals("RP", metaphone.metaphone("Rp"));
        assertEquals("RQ", metaphone.metaphone("Qr"));
        assertEquals("RR", metaphone.metaphone("Rr"));
        assertEquals("RT", metaphone.metaphone("Rt"));
        assertEquals("RU", metaphone.metaphone("Rr"));
        assertEquals("RY", metaphone.metaphone("Ry"));
        assertEquals("RZ", metaphone.metaphone("Rz"));
        assertEquals("SB", metaphone.metaphone("Sb"));
        assertEquals("SC", metaphone.metaphone("Sc"));
        assertEquals("SL", metaphone.metaphone("Sl"));
        assertEquals("SP", metaphone.metaphone("Sp"));
        assertEquals("ST", metaphone.metaphone("St"));
        assertEquals("SY", metaphone.metaphone("Sy"));
        assertEquals("SZ", metaphone.metaphone("Sz"));
        assertEquals("TC", metaphone.metaphone("Tc"));
        assertEquals("TD", metaphone.metaphone("Td"));
        assertEquals("TF", metaphone.metaphone("Tf"));
        assertEquals("TH", metaphone.metaphone("Th"));
        assertEquals("TI", metaphone.metaphone("Ti"));
        assertEquals("TJ", metaphone.metaphone("Tj"));
        assertEquals("TK", metaphone.metaphone("Tk"));
        assertEquals("TL", metaphone.metaphone("Tl"));
        assertEquals("TP", metaphone.metaphone("Tp"));
        assertEquals("TQ", metaphone.metaphone("Tq"));
        assertEquals("TR", metaphone.metaphone("Tr"));
        assertEquals("TS", metaphone.metaphone("Ts"));
        assertEquals("TT", metaphone.metaphone("Tt"));
        assertEquals("TY", metaphone.metaphone("Ty"));
        assertEquals("TZ", metaphone.metaphone("Tz"));
        assertEquals("VB", metaphone.metaphone("Vb"));
        assertEquals("VC", metaphone.metaphone("Vc"));
        assertEquals("VL", metaphone.metaphone("Vl"));
        assertEquals("VP", metaphone.metaphone("Vp"));
        assertEquals("VQ", metaphone.metaphone("Vq"));
        assertEquals("VR", metaphone.metaphone("Vr"));
        assertEquals("VT", metaphone.metaphone("Vt"));
        assertEquals("VY", metaphone.metaphone("Vy"));
        assertEquals("VZ", metaphone.metaphone("Vz"));
        assertEquals("WC", metaphone.metaphone("Wc"));
        assertEquals("WD", metaphone.metaphone("Wd"));
        assertEquals("WF", metaphone.metaphone("Wf"));
        assertEquals("WH", metaphone.metaphone("Wh"));
        assertEquals("WI", metaphone.metaphone("Wi"));
        assertEquals("WK", metaphone.metaphone("Wk"));
        assertEquals("WR", metaphone.metaphone("Wr"));
        assertEquals("WT", metaphone.metaphone("Wt"));
        assertEquals("WY", metaphone.metaphone("Wy"));
        assertEquals("WZ", metaphone.metaphone("Wz"));
        assertEquals("XB", metaphone.metaphone("Xb"));
        assertEquals("XC", metaphone.metaphone("Xc"));
        assertEquals("XL", metaphone.metaphone("Xl"));
        assertEquals("XN", metaphone.metaphone("Xn"));
        assertEquals("XP", metaphone.metaphone("Xp"));
        assertEquals("XQ", metaphone.metaphone("Xq"));
        assertEquals("XR", metaphone.metaphone("Xr"));
        assertEquals("XT", metaphone.metaphone("XT"));
        assertEquals("XY", metaphone.metaphone("Xy"));
        assertEquals("XZ", metaphone.metaphone("Xz"));
        assertEquals("YB", metaphone.metaphone("Yb"));
        assertEquals("YC", metaphone.metaphone("Yc"));
        assertEquals("YF", metaphone.metaphone("Yf"));
        assertEquals("YG", metaphone.metaphone("Yg"));
        assertEquals("YH", metaphone.metaphone("Yh"));
        assertEquals("YJ", metaphone.metaphone("Yj"));
        assertEquals("YK", metaphone.metaphone("Yk"));
        assertEquals("YL", metaphone.metaphone("Yl"));
        assertEquals("YP", metaphone.metaphone("Yp"));
        assertEquals("YR", metaphone.metaphone("Yr"));
        assertEquals("YS", metaphone.metaphone("Ys"));
        assertEquals("YT", metaphone.metaphone("Yt"));
        assertEquals("YU", metaphone.metaphone("Yu"));
        assertEquals("YW", metaphone.metaphone("Yw"));
        assertEquals("YX", metaphone.metaphone("Yx"));
        assertEquals("YY", metaphone.metaphone("Yy"));
        assertEquals("YZ", metaphone.metaphone("Yz"));
        assertEquals("ZB", metaphone.metaphone("Zb"));
        assertEquals("ZC", metaphone.metaphone("Zc"));
        assertEquals("ZF", metaphone.metaphone("Zf"));
        assertEquals("ZG", metaphone.metaphone("Zg"));
        assertEquals("ZH", metaphone.metaphone("Zh"));
        assertEquals("ZJ", metaphone.metaphone("Zj"));
        assertEquals("ZK", metaphone.metaphone("Zk"));
        assertEquals("ZL", metaphone.metaphone("Zl"));
        assertEquals("ZM", metaphone.metaphone("Zm"));
        assertEquals("ZN", metaphone.metaphone("Zn"));
        assertEquals("ZP", metaphone.metaphone("Zp"));
        assertEquals("ZQ", metaphone.metaphone("Zq"));
        assertEquals("ZR", metaphone.metaphone("Zr"));
        assertEquals("ZS", metaphone.metaphone("Zs"));
        assertEquals("ZT", metaphone.metaphone("Zt"));
        assertEquals("ZU", metaphone.metaphone("Zu"));
        assertEquals("ZW", metaphone.metaphone("Zw"));
        assertEquals("ZX", metaphone.metaphone("Zx"));
        assertEquals("ZZ", metaphone.metaphone("Zz"));
    }

    @Test(expected = EncoderException.class)
    public void testNonStringObject() {
        metaphone.encode(123);
    }

    @Test
    public void testMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }
}