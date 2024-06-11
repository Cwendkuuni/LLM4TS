package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinishWithoutPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));
        assertThrows(IllegalStateException.class, tarArchiveOutputStream::finish);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));

        byte[] expectedEOFRecord = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        tarArchiveOutputStream.writeEOFRecord();
        byte[] actualEOFRecord = tarArchiveOutputStream.buffer.getBuffer();
        assertArrayEquals(expectedEOFRecord, actualEOFRecord);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024));

        String testString = "Hello, World!";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.write(testBytes, 0, testBytes.length);
        byte[] actualBytes = tarArchiveOutputStream.buffer.getBuffer();

        assertEquals(testBytes.length, tarArchiveOutputStream.currBytes);
        assertEquals(testString, new String(Arrays.copyOfRange(actualBytes, 512, 512 + testString.length()), StandardCharsets.UTF_8));
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/ca/cb/cc/cd/ce/cf/cg/ch/ci/cj/ck/cl/cm/cn/co/cp/cq/cr/cs/ct/cu/cv/cw/cx/cy/cz/da/db/dc/dd/de/df/dg/dh/di/dj/dk/dl/dm/dn/do/dp/dq/dr/ds/dt/du/dv/dw/dx/dy/dz/ea/eb/ec/ed/ee/ef/eg/eh/ei/ej/ek/el/em/en/eo/ep/eq/er/es/et/eu/ev/ew/ex/ey/ez/fa/fb/fc/fd/fe/ff/fg/fh/fi/fj/ fk/fl/fm/fn/fo/fp/fq/fr/fs/ft/fu/fv/fw/fx/fy/fz/ga/gb/gc/gd/ge/gf/gg/gh/gi/gj/gk/gl/gm/gn/go/gp/gq/gr/gs/gt/gu/gv/gw/gx/gy/gz/ha/hb/hc/hd/he/hf/hg/hh/hi/hj/hk/hl/hm/hn/ho/hp/hq/hr/hs/ht/hu/hv/hw/hx/hy/hz/ia/ib/ic/id/ie/if/ig/ih/ii/ij/ik/il/im/in/io/ip/iq/ir/is/it/iu/iv/iw/ix/iy/iz/ja/jb/jc/jd/je/jf/jg/jh/ji/jj/jk/jl/jm/jn/jo/jp/jq/jr/js/jt/ju/jv/jw/jx/jy/jz/ka/kb/kc/kd/ke/kf/kg/kh/ki/kj/kk/kl/km/kn/ko/kp/kq/kr/ks/kt/ku/kv/kw/kx/ky/kz/la/lb/lc/ld/le/lf/lg/lh/li/lj/lk/ll/lm/ln/lo/lp/lq/lr/ls/lt/lu/lv/lw/lx/ly/lz/ma/mb/mc/md/me/mf/mg/mh/mi/mj/mk/ml/mm/mn/mo/mp/mq/mr/ms/mt/mu/mv/mw/mx/my/mz/na/nb/nc/nd/ne/nf/ng/nh/ni/nj/nk/nl/nm/nn/no/np/nq/nr/ns/nt/nu/nv/nw/nx/ny/nz/oa/ob/oc/od/oe/of/og/oh/oi/oj/ok/ol/om/on/oo/op/oq/or/os/ot/ou/ov/ow/ox/oy/oz/pa/pb/pc/pd/pe/pf/pg/ph/pi/pj/pk/pl/pm/pn/po/pp/pq/pr/ps/pt/pu/pv/pw/px/py/pz/qa/qb/qc/qd/qe/qf/qg/qh/qi/qj/qk/ql/qm/qn/qo/qp/qr/qs/qt/qu/qv/qw/qx/qy/qz/ra/rb/rc/rd/re/rf/rg/rh/ri/rj/rk/rl/rm/rn/ro/rp/rq/rr/rs/rt/ru/rv/rw/rx/ry/rz/sa/sb/sc/sd/se/sf/sg/sh/si/sj/sk/sl/sm/sn/so/sp/sq/sr/ss/st/su/sv/sw/sx/sy/sz/ta/tb/tc/td/te/tf/tg/th/ti/tj/tk/tl/tm/tn/to/tp/tq/tr/ts/tt/tu/tv/tw/tx/ty/tz/ua/ub/uc/ud/ue/uf/ug/uh/ui/uj/uk/ul/um/un/uo/up/uq/ur/us/ut/uu/uv/uw/ux/uy/uz/va/vb/vc/vd/ve/vf/vg/vh/vi/vj/vk/vl/vm/vn/vo/vp/vq/vr/vs/vt/vu/vv/vw/vx/vy/vz/wa/wb/wc/wd/we/wf/wg/wh/wi/wj/wk/wl/wm/wn/wo/wp/wq/wr/ws/wt/wu/wv/ww/wx/wy/wz/ya/yb/yc/yd/ye/yf/yg/yh/yi/yj/yk/yl/ym/yn/yo/yp/yq/yr/ys/yt/yu/yv/yw/yx/yy/yz/za/zb/zc/zd/ze/zf/zg/zh/zi/zj/zk/zl/zm/zn/zo/zp/zq/zr/zs/zt/zu/zv/zw/zx/zy/zz";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("src/test/resources/" + longFileName), longFileName);

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(longFileName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.write(0);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] actualBytes = tarArchiveOutputStream.buffer.getBuffer();

        assertEquals(TarConstants.TAR_HEADER_SIZE + longFileName.length() + 1, tarArchiveOutputStream.currSize);
        assertEquals(longFileName, new String(Arrays.copyOfRange(actualBytes, 512, 512 + longFileName.length()), StandardCharsets.UTF_8));
    }

    @Test
    public void testPutArchiveEntryWithoutLongFileName() throws IOException {
        String shortFileName = "test.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("src/test/resources/" + shortFileName), shortFileName);

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();

        byte[] actualBytes = tarArchiveOutputStream.buffer.getBuffer();

        assertEquals(TarConstants.TAR_HEADER_SIZE, TarConstants.TAR_HEADER_SIZE + shortFileName.length(), tarArchiveOutputStream.currSize);
        assertEquals(0, Arrays.compare(actualBytes, 512, TarConstants.TAR_HEADER_SIZE, shortFileName.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testCloseArchiveEntryWithoutWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("src/test/resources/empty.txt"), "empty.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] actualBytes = tarArchiveOutputStream.buffer.getBuffer();

        assertEquals(0, actualBytes.length);
    }

    @Test
    public void testCloseArchiveEntryWithWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("src/test/resources/empty.txt"), "empty.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        byte[] testBytes = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testBytes, 0, testBytes.length);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] actualBytes = tarArchiveOutputStream.buffer.getBuffer();

        assertEquals(testBytes.length, tarArchiveOutputStream.currBytes);
        assertEquals("Hello, World!", new String(Arrays.copyOfRange(actualBytes, 512, 512 + testBytes.length()), StandardCharsets.UTF_8));
    }
}