import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new TarArchiveOutputStream(byteArrayOutputStream, 1024, 512);
    }

    @Test
    public void testSimplePutArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        if (testFile.exists()) {
            testFile.delete();
        }
        testFile.createNewFile();

        TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt");
        outputStream.putArchiveEntry(testEntry);
        writeStringToFile(testFile, "Test String");
        outputStream.write(new byte[4096], 0, 4096);
        outputStream.close();

        Assert.assertEquals(1, getNumberOfBytesInArchive());
        Assert.assertEquals("testFile.txt", getNameOfFirstEntry());

        TarArchiveEntry archiveEntry = outputStream.getNextEntry();
        Assert.assertNotNull(archiveEntry);
        Assert.assertEquals("testFile.txt", archiveEntry.getName());

        byte[] bytes = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = getFileInputStream(testFile).read(bytes, 0, 4096)) != -1) {
            outputStream.write(bytes, 0, bytesRead);
        }
        outputStream.close();
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLongFileNamePutArchiveEntry() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/ca/cb/cc/cd/ce/cf/cg/ch/ci/cj/ck/cl/cm/cn/co/cp/cq/cr/cs/ct/cu/cv/cw/cx/cy/cz/da/db/dc/dd/de/df/dg/dh/di/dj/dk/dl/dm/dn/do/dp/dq/dr/ds/dt/du/dv/dw/dx/dy/dz/ea/eb/ec/ed/ee/ef/eg/eh/ei/ej/ek/el/em/en/eo/ep/eq/er/es/et/eu/ev/ew/ex/ey/ez/fa/fb/fc/fd/fe/ff/fg/fh/fi/fj/fk/fl/fm/fn/fo/fp/fq/fr/fs/ft/fu/fv/fw/fx/fy/fz/ga/gb/gc/gd/ge/gf/gg/gh/gi/gj/gk/gl/gm/gn/go/gp/gq/gr/gs/gt/gu/gv/gw/gx/gy/gz/ha/hb/hc/hd/he/hf/hg/hh/hi/hj/hk/hl/hm/hn/ho/hp/hq/hr/hs/ht/hu/hv/hw/hx/hy/hz/ia/ib/ic/id/ie/if/ig/ih/ii/ij/ik/il/im/in/io/ip/iq/ir/is/it/iu/iv/iw/ix/iy/iz/ja/jb/jc/jd/je/jf/jg/jh/ji/jj/jk/jl/jm/jn/jo/jp/jq/jr/js/jt/ju/jv/jw/jx/jy/jz/ka/kb/kc/kd/ke/kf/kg/kh/ki/kj/kk/kl/km/kn/ko/kp/kq/kr/ks/kt/ku/kv/kw/kx/ky/kz/la/lb/lc/ld/le/lf/lg/lh/li/lj/lk/ll/lm/ln/lo/lp/lq/lr/ls/lt/lu/lv/lw/lx/ly/lz/ma/mb/mc/md/me/mf/mg/mh/mi/mj/mk/ml/mm/mn/mo/mp/mq/mr/ms/mt/mu/mv/mw/mx/my/mz/na/nb/nc/nd/ne/nf/ng/nh/ni/nj/nk/nl/nm/nn/no/np/nq/nr/ns/nt/nu/nv/nw/nx/ny/nz/oa/ob/oc/od/oe/of/og/oh/oi/oj/ok/ol/om/on/oo/op/oq/or/os/ot/ou/ov/ow/ox/oy/oz/pa/pb/pc/pd/pe/pf/pg/ph/pi/pj/pk/pl/pm/pn/po/pp/pq/pr/ps/pt/pu/pv/pw/px/py/pz/qa/qb/qc/qd/qe/qf/qg/qh/qi/qj/qk/ql/qm/qn/qo/qp/qr/qs/qt/qu/qv/qw/qx/qy/qz/ra/rb/rc/rd/re/rf/rg/rh/ri/rj/rk/rl/rm/rn/ro/rp/rq/rr/rs/rt/ru/rv/rw/rx/ry/rz/sa/sb/sc/sd/se/sf/sg/sh/si/sj/sk/sl/sm/sn/so/sp/sq/sr/ss/st/su/sv/sw/sx/sy/sz/ta/tb/tc/td/te/tf/tg/th/ti/tj/tk/tl/tm/tn/to/tp/tq/tr/ts/tt/tu/tv/tw/tx/ty/tz/ua/ub/uc/ud/ue/uf/ug/uh/ui/uj/uk/ul/um/un/uo/up/uq/ur/us/ut/uu/uv/uw/ux/uy/uz/va/vb/vc/vd/ve/vf/vg/vh/vi/vj/vk/vl/vm/vn/vo/vp/vq/vr/vs/vt/vu/vv/vw/vx/vy/vz/wa/wb/wc/wd/we/wf/wg/wh/wi/wj/wk/wl/wm/wn/wo/wp/wq/wr/ws/wt/wu/vv/ww/wx/wy/wz/xa/xb/xc/xd/xe/xf/xg/xh/xi/xj/xk/xl/xm/xn/xo/xp/xq/xr/xs/xt/xu/xv/xw/xx/xy/xz/ya/yb/yc/yd/ye/yf/yg/yh/yi/yj/yk/yl/ym/yn/yo/yp/yq/yr/ys/yt/yu/yv/yw/xy/yy/yz/za/zb/zc/zd/ze/zf/zg/zh/zi/zj/zk/zl/zm/zn/zo/zp/zq/zr/zs/zt/zu/zv/zw/zx/zy/zz";
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(longFileName);
        outputStream.putArchiveEntry(longFileNameEntry);
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNegativeSize() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt", -1);
        outputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        Object nonTarArchiveEntry = new Object();
        outputStream.putArchiveEntry(nonTarArchiveEntry);
    }

    @Test
    public void testPutMultipleArchiveEntries() throws IOException {
        TarArchiveEntry testEntry1 = new TarArchiveEntry("testFile1.txt");
        TarArchiveEntry testEntry2 = new TarArchiveEntry("testFile2.txt");

        outputStream.putArchiveEntry(testEntry1);
        outputStream.putArchiveEntry(testEntry2);
        outputStream.write(new byte[4096], 0, 4096);
        outputStream.finish();
        outputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarArchiveOutputStream newOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry archiveEntry1 = newOutputStream.getNextEntry();
        TarArchiveEntry archiveEntry2 = newOutputStream.getNextEntry();
        Assert.assertNotNull(archiveEntry1);
        Assert.assertEquals("testFile1.txt", archiveEntry1.getName());
        Assert.assertNotNull(archiveEntry2);
        Assert.assertEquals("testFile2.txt", archiveEntry2.getName());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPuttingOne() throws IOException {
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testWritingToFileAfterClosing() throws IOException {
        File testFile = new File("testFile.txt");
        if (testFile.exists()) {
            testFile.delete();
        }
        testFile.createNewFile();

        outputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        writeStringToFile(testFile, "Test String");
        outputStream.write(new byte[4096], 0, 4096);
        outputStream.close();

        byte[] bytes = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = getFileInputStream(testFile).read(bytes, 0, 4096)) != -1) {
            outputStream.write(bytes, 0, bytesRead);
        }
        outputStream.close();

        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testWritingAfterFinishButBeforeClose() throws IOException {
        outputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        writeStringToFile(new File("testFile.txt"), "Test String");
        outputStream.write(new byte[4096], 0, 4096);
        outputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testWritingAfterClose() throws IOException {
        outputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        outputStream.write(new byte[4096], 0, 4096);
        outputStream.close();
        outputStream.write(new byte[4096], 0, 4096);
    }

    @Test
    public void testWritingToEmptyArchive() throws IOException {
        TarArchiveOutputStream emptyOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        emptyOutputStream.finish();
        emptyOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(emptyOutputStream.toByteArray());
        TarArchiveOutputStream newOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry archiveEntry1 = newOutputStream.getNextEntry();
        Assert.assertNull(archiveEntry1);
    }

    private int getNumberOfBytesInArchive() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarArchiveOutputStream newOutputStream = new TarArchiveOutputStream(inputStream);
        int numberOfEntries = 0;
        while ((newOutputStream.getNextEntry() != null)) {
            numberOfEntries++;
        }
        return numberOfEntries * 512;
    }

    private String getNameOfFirstEntry() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        TarArchiveOutputStream newOutputStream = new TarArchiveOutputStream(inputStream);
        TarArchiveEntry archiveEntry = newOutputStream.getNextEntry();
        return archiveEntry.getName();
    }

    private File getFileInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }

    private void writeStringToFile(File file, String text) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }
}