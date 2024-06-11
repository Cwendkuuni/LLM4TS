package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        byte[] eofRecord = new byte[TarConstants.TAR_BLOCKSIZE];
        for (int i = 0; i < TarConstants.TAR_BLOCKSIZE; i++) {
            eofRecord[i] = 0;
        }
        assertArrayEquals(eofRecord, outputStream.toByteArray(), 0, eofRecord.length);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        byte[] eofRecord = new byte[TarConstants.TAR_BLOCKSIZE];
        for (int i = 0; i < TarConstants.TAR_BLOCKSIZE; i++) {
            eofRecord[i] = 0;
        }
        assertArrayEquals(eofRecord, outputStream.toByteArray(), 0, eofRecord.length);
        tarArchiveOutputStream.close();
        byte[] closeRecord = new byte[TarConstants.TAR_BLOCKSIZE];
        for (int i = 0; i < TarConstants.TAR_BLOCKSIZE; i++) {
            closeRecord[i] = 0;
        }
        assertArrayEquals(closeRecord, outputStream.toByteArray(), 0, closeRecord.length);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        assertArrayEquals(closeRecord, outputStream.toByteArray(), 0, closeRecord.length);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Test content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expected = new byte[TarConstants.TAR_HEADER_LENGTH + "test.txt".length + 81];
        for (int i = 0; i < TarConstants.TAR_HEADER_LENGTH; i++) {
            expected[i] = (byte) 0;
        }
        for (int i = TarConstants.TAR_HEADER_LENGTH; i < TarConstants.TAR_HEADER_LENGTH + "test.txt".length; i++) {
            expected[i] = "test.txt".charAt(i - TarConstants.TAR_HEADER_LENGTH) & 0xFF;
        }
        for (int i = TarConstants.TAR_HEADER_LENGTH + "test.txt".length; i < TarConstants.TAR_HEADER_LENGTH + "test.txt".length + 81; i++) {
            expected[i] = 'T' & 0xFF;
        }
        assertArrayEquals(expected, outputStream.toByteArray(), 0, expected.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/ca/cb/cc/cd/ce/cf/cg/ch/ci/cj/ck/cl/cm/cn/co/cp/cq/cr/cs/ct/cu/cv/cw/cx/cy/cz/da/db/dc/dd/de/df/dg/dh/di/dj/dk/dl/dm/dn/do/dp/dq/dr/ds/dt/du/dv/dw/dx/dy/dz/ea/eb/ec/ed/ee/ef/eg/eh/ei/ej/ek/el/em/en/eo/ep/eq/er/es/et/eu/ev/ew/ex/ey/ez/fa/fb/fc/fd/fe/ff/fg/fh/fi/fj/fk/fl/fm/fn/fo/fp/fq/fr/fs/ft/fu/fv/fw/fx/fy/fz/ga/gb/gc/gd/ge/gf/gg/gh/gi/gj/gk/gl/gm/gn/go/gp/gq/gr/gs/gt/gu/gv/gw/gx/gy/gz/ha/hb/hc/hd/he/hf/hg/hh/hi/hj/hk/hl/hm/hn/ho/hp/hq/hr/hs/ht/hu/hv/hw/hx/hy/hz/ia/ib/ic/id/ie/if/ig/ih/ii/ij/ik/il/im/in/io/ip/iq/ir/is/it/iu/iv/iw/ix/iy/iz/ja/jb/jc/jd/je/jf/jg/jh/ji/jj/jk/jl/jm/jn/jo/jp/jq/jr/js/jt/ju/jv/jw/jx/jy/jz/ka/kb/kc/kd/ke/kf/kg/kh/ki/kj/kk/kl/km/kn/ko/kp/kq/kr/ks/kt/ku/kv/kw/kx/ky/kz/la/lb/lc/ld/le/lf/lg/lh/li/lj/lk/ll/lm/ln/lo/lp/lq/lr/ls/lt/lu/lv/lw/lx/ly/lz/ma/mb/mc/md/me/mf/mg/mh/mi/mj/mk/ml/mm/mn/mo/mp/mq/mr/ms/mt/mu/mv/mw/mx/my/mz/na/nb/nc/nd/ne/nf/ng/nh/ni/nj/nk/nl/nm/nn/no/np/nq/nr/ns/nt/nu/nv/nw/nx/ny/nz/oa/ob/oc/od/oe/of/og/oh/oi/oj/ok/ol/om/on/oo/op/oq/or/os/ot/ou/ov/ow/ox/oy/oz/pa/pb/pc/pd/pe/pf/pg/ph/pi/pj/pk/pl/pm/pn/po/pp/pq/pr/ps/pt/pu/pv/pw/px/py/pz/qa/qb/qc/qd/qe/qf/qg/qh/qi/qj/qk/ql/qm/qn/qo/qp/qr/qs/qt/qu/qv/qw/qx/qy/qz/ra/rb/rc/rd/re/rf/rg/rh/ri/rj/rk/rl/rm/rn/ro/rp/rq/rs/rt/ru/rv/rw/rx/ry/rz/sa/sb/sc/sd/se/sf/sg/sh/si/sj/sk/sl/sm/sn/so/sp/sq/sr/ss/st/su/sv/sw/sx/sy/sz/ta/tb/tc/td/te/tf/tg/th/ti/tj/tk/tl/tm/tn/to/tp/tq/tr/ts/tt/tu/tv/tw/tx/ty/tz/ua/ub/uc/ud/ue/uf/ug/uh/ui/uj/uk/ul/um/un/uo/up/uq/ur/us/ut/uu/uv/uw/ux/uy/uz/va/vb/vc/vd/ve/vf/vg/vh/vi/vj/vk/vl/vm/vn/vo/vp/vq/vr/vs/vt/vu/vv/vw/vx/vy/vz/wa/wb/wc/wd/we/wf/wg/wh/wi/wj/wk/wl/wm/wn/wo/wp/wq/wr/ws/wt/wu/wv/ww/wx/wy/wz/xa/xb/xc/xd/xe/xf/xg/xh/xi/xj/xk/xl/xm/xn/xo/xp/xq/xr/xs/xt/xu/xv/xw/xx/xy/xz/ya/yb/yc/yd/ye/yf/yg/yh/yi/yj/yk/yl/ym/yn/yo/yp/yq/yr/ys/yt/yu/yv/yw/xy/yy/yz/za/zb/zc/zd/ze/zf/zg/zh/zi/zj/zk/zl/zm/zn/zo/zp/zq/zr/zs/zt/zu/zv/zw/zx/zy/zz";
        TarArchiveEntry entry = new TarArchiveEntry(new File("."), longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Test content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expected = new byte[TarConstants.TAR_BLOCKSIZE + TarConstants.TAR_HEADER_LENGTH + longFileName.length() + "Test content".length()];
        for (int i = 0; i < TarConstants.TAR_BLOCKSIZE; i++) {
            expected[i] = 0;
        }
        for (int i = TarConstants.TAR_BLOCKSIZE; i < expected.length; i++) {
            if (i >= TarConstants.TAR_BLOCKSIZE && i < TarConstants.TAR_BLOCKSIZE + TarConstants.TAR_HEADER_LENGTH + longFileName.length()) {
                expected[i] = (byte) (longFileName.charAt(i - TarConstants.TAR_BLOCKSIZE - TarConstants.TAR_HEADER_LENGTH) & 0xFF);
            } else if (i >= TarConstants.TAR_BLOCKSIZE + TarConstants.TAR_HEADER_LENGTH + longFileName.length() && i < expected.length - "Test content".length()) {
                expected[i] = 0;
            } else {
                expected[i] = "Test content".charAt(i - TarConstants.TAR_BLOCKSIZE - TarConstants.TAR_HEADER_LENGTH - longFileName.length() - 1) & 0xFF;
            }
        }
        assertArrayEquals(expected, outputStream.toByteArray(), 0, expected.length);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Test content".getBytes());
        byte[] data = tarArchiveOutputStream.getBuffer();
        assertNull(tarArchiveOutputStream.closeArchiveEntry());
        byte[] expected = new byte[TarConstants.TAR_BLOCKSIZE];
        for (int i = 0; i < TarConstants.TAR_BLOCKSIZE; i++) {
            expected[i] = data[i];
        }
        assertArrayEquals(expected, tarArchiveOutputStream.getBuffer(), 0, expected.length);
        assertNull(tarArchiveOutputStream.closeArchiveEntry());
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Test content".getBytes(), 2, "Test content".length() - 2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close();
    }
}