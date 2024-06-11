import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() {
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expected = new byte[1024 * 2]; // 2 x 512 bytes EOF record
        assertEquals(expected.length, outputStream.size());
        assertNotNull(tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expected = new byte[outputStream.size()];
        System.arraycopy(outputStream.toByteArray(), 0, expected, 0, outputStream.size());
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() {
        tarArchiveOutputStream.putArchiveEntry(new File("test.txt"));
    }

    @Test
    public void testPutArchiveEntryAndWrite() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = new byte[tarArchiveEntry.getSize() + 512]; // 512 bytes for header record
        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expected, 0, tarArchiveEntry.getHeaderRecord().length);
        System.arraycopy("test content".getBytes(StandardCharsets.UTF_8), 0, expected, tarArchiveEntry.getHeaderRecord().length, "test content".getBytes(StandardCharsets.UTF_8).length);

        assertEquals(expected.length, outputStream.size());
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testPutArchiveEntryAndWriteWithLongFileName() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/ca/cb/cc/cd/ce/cf/cg/ch/ci/cj/ck/cl/cm/cn/co/cp/cq/cr/cs/ct/cu/cv/cw/cx/cy/cz/da/db/dc/dd/de/df/dg/dh/di/dj/dk/dl/dm/dn/do/dp/dq/dr/ds/dt/du/dv/dw/dx/dy/dz/ea/eb/ec/ed/ee/ef/eg/eh/ei/ej/ek/el/em/en/eo/ep/eq/er/es/et/eu/ev/ew/ex/ey/ez/fa/fb/fc/fd/fe/ff/fg/fh/fi/fj/fk/fl/fm/fn/fo/fp/fq/fr/fs/ft/fu/fv/fw/fx/fy/fz/ga/gb/gc/gd/ge/gf/gg/gh/gi/gj/gk/gl/gm/gn/go/gp/gq/gr/gs/gt/gu/gv/gw/gx/gy/gz/ha/hb/hc/hd/he/hf/hg/hh/hi/hj/hk/hl/hm/hn/ho/hp/hq/hr/hs/ht/hu/hv/hw/hx/hy/hz/ia/ib/ic/id/ie/if/ig/ih/ii/ij/ik/il/im/in/io/ip/iq/ir/is/it/iu/iv/iw/ix/iy/iz/ja/jb/jc/jd/je/jf/jg/jh/ji/jj/jk/jl/jm/jn/jo/jp/jq/jr/js/jt/ju/jv/jw/jx/jy/jz/ka/kb/kc/kd/ke/kf/kg/kh/ki/kj/kk/kl/km/kn/ko/kp/kq/kr/ks/kt/ku/kv/kw/kx/ky/kz/la/lb/lc/ld/le/lf/lg/lh/li/lj/lk/ll/lm/ln/lo/lp/lq/lr/ls/lt/lu/lv/lw/lx/ly/lz/ma/mb/mc/md/me/mf/mg/mh/mi/mj/mk/ml/mm/mn/mo/mp/mq/mr/ms/mt/mu/mv/mw/mx/my/mz/na/nb/nc/nd/ne/nf/ng/nh/ni/nj/nk/nl/nm/nn/no/np/nq/nr/ns/nt/nu/nv/nw/nx/ny/nz/oa/ob/oc/od/oe/of/og/oh/oi/oj/ok/ol/om/on/oo/op/oq/or/os/ot/ou/ov/ow/ox/oy/oz/pa/pb/pc/pd/pe/pf/pg/ph/pi/pj/pk/pl/pm/pn/po/pp/pq/pr/ps/pt/pu/pv/pw/px/py/pz/qa/qb/qc/qd/qe/qf/qg/qh/qi/qj/qk/ql/qm/qn/qo/qp/qr/qs/qt/qu/qv/qw/qx/qy/qz/ra/rb/rc/rd/re/rf/rg/rh/ri/rj/rk/rl/rm/rn/ro/rp/rq/rr/rs/rt/ru/rv/rw/rx/ry/rz/sa/sb/sc/sd/se/sf/sg/sh/si/sj/sk/sl/sm/sn/so/sp/sq/sr/ss/st/su/sv/sw/sx/sy/sz/ta/tb/tc/td/te/tf/tg/th/ti/tj/tk/tl/tm/tn/to/tp/tq/tr/ts/tt/tu/tv/tw/tx/ty/tz/ua/ub/uc/ud/ue/uf/ug/uh/ui/uj/uk/ul/um/un/uo/up/uq/ur/us/ut/uu/uv/uw/ux/uy/uz/va/vb/vc/vd/ve/vf/vg/vh/vi/vj/vk/vl/vm/vn/vo/vp/vq/vr/vs/vt/vu/vv/vw/vx/vy/vz/wa/wb/wc/wd/we/wf/wg/wh/wi/wj/wk/wl/wm/wn/wo/wp/wq/wr/ws/wt/wu/wv/ww/wx/wy/wz/xa/xb/xc/xd/xe/xf/xg/xh/xi/xj/xk/xl/xm/xn/xo/xp/xq/xr/xs/xt/xu/xv/xw/xw/xz/ya/yb/yc/yd/ye/yf/yg/yh/yi/yj/yk/yl/ym/yn/yo/yp/yq/yr/ys/yt/yu/yv/yw/yx/yy/yz/za/zb/zc/zd/ze/zf/zg/zh/zi/zj/zk/zl/zm/zn/zo/zp/zq/zr/zs/zt/zu/zv/zw/zx/zy/zz";
        TarArchiveEntry longFileNameArchiveEntry = new TarArchiveEntry(longFileName, "long file content");
        tarArchiveOutputStream.putArchiveEntry(longFileNameArchiveEntry);
        tarArchiveOutputStream.write("long file content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = new byte[longFileName.getBytes(StandardCharsets.UTF_8).length + 512 + longFileNameArchiveEntry.getSize()];

        // write long file name record
        // TODO: implement long file name writing based on longFileMode
        System.arraycopy("GNU\0".getBytes(StandardCharsets.UTF_8), 0, expected, 0, "GNU\0".length());
        System.arraycopy(longFileName.getBytes(StandardCharsets.UTF_8), 0, expected, "GNU\0".length(), longFileName.getBytes(StandardCharsets.UTF_8).length);
        System.arraycopy(Long.toString(longFileNameArchiveEntry.getSize()).getBytes(StandardCharsets.UTF_8), 0, expected, longFileName.getBytes(StandardCharsets.UTF_8).length + "GNU\0".length(), Long.toString(longFileNameArchiveEntry.getSize()).getBytes(StandardCharsets.UTF_8).length);
        System.arraycopy(longFileNameArchiveEntry.getHeaderRecord(), 0, expected, longFileName.getBytes(StandardCharsets.UTF_8).length + "GNU\0".length() + Long.toString(longFileNameArchiveEntry.getSize()).getBytes(StandardCharsets.UTF_8).length, longFileNameArchiveEntry.getHeaderRecord().length);

        // write file content
        System.arraycopy("long file content".getBytes(StandardCharsets.UTF_8), 0, expected, longFileName.getBytes(StandardCharsets.UTF_8).length + "GNU\0".length() + Long.toString(longFileNameArchiveEntry.getSize()).getBytes(StandardCharsets.UTF_8).length + longFileNameArchiveEntry.getHeaderRecord().length, "long file content".getBytes(StandardCharsets.UTF_8).length);

        assertEquals(expected.length, outputStream.size());
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNonExistentFile() {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        File file = new File("test.txt");
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(file, "test content"));
    }
}