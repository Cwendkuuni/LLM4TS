package com.hf.sfm.crypt;

public class Base64
{
    private static final byte[] _$23169;
    private static final byte[] _$23168;
    private static final char[] _$23167;
    private static final char[] _$23166;
    
    public static byte[] altBase64ToByteArray(final String s) {
        return _$23180(s, true);
    }
    
    private static byte[] _$23180(final String s, final boolean flag) {
        final byte[] bb = flag ? Base64._$23169 : Base64._$23168;
        final int i = s.length();
        final int j = i / 4;
        if (4 * j != i) {
            throw new IllegalArgumentException("String length must be a multiple of four.");
        }
        int k = 0;
        int l = j;
        if (i != 0) {
            if (s.charAt(i - 1) == '=') {
                ++k;
                --l;
            }
            if (s.charAt(i - 2) == '=') {
                ++k;
            }
        }
        final byte[] bc = new byte[3 * j - k];
        int i2 = 0;
        int j2 = 0;
        for (int k2 = 0; k2 < l; ++k2) {
            final int l2 = _$23183(s.charAt(i2++), bb);
            final int j3 = _$23183(s.charAt(i2++), bb);
            final int l3 = _$23183(s.charAt(i2++), bb);
            final int j4 = _$23183(s.charAt(i2++), bb);
            bc[j2++] = (byte)(l2 << 2 | j3 >> 4);
            bc[j2++] = (byte)(j3 << 4 | l3 >> 2);
            bc[j2++] = (byte)(l3 << 6 | j4);
        }
        if (k != 0) {
            final int i3 = _$23183(s.charAt(i2++), bb);
            final int k3 = _$23183(s.charAt(i2++), bb);
            bc[j2++] = (byte)(i3 << 2 | k3 >> 4);
            if (k == 1) {
                final int i4 = _$23183(s.charAt(i2++), bb);
                bc[j2++] = (byte)(k3 << 4 | i4 >> 2);
            }
        }
        return bc;
    }
    
    public static byte[] base64ToByteArray(final String s) {
        return _$23180(s, false);
    }
    
    private static int _$23183(final char c, final byte[] bb) {
        final byte b = bb[c];
        if (b < 0) {
            throw new IllegalArgumentException("Illegal character " + c);
        }
        return b;
    }
    
    public static String byteArrayToAltBase64(final byte[] bb) {
        return _$23170(bb, true);
    }
    
    private static String _$23170(final byte[] bb, final boolean flag) {
        final int i = bb.length;
        final int j = i / 3;
        final int k = i - 3 * j;
        final int l = 4 * ((i + 2) / 3);
        final StringBuffer stringbuffer = new StringBuffer(l);
        final char[] ac = flag ? Base64._$23167 : Base64._$23166;
        int i2 = 0;
        for (int j2 = 0; j2 < j; ++j2) {
            final int k2 = bb[i2++] & 0xFF;
            final int i3 = bb[i2++] & 0xFF;
            final int k3 = bb[i2++] & 0xFF;
            stringbuffer.append(ac[k2 >> 2]);
            stringbuffer.append(ac[(k2 << 4 & 0x3F) | i3 >> 4]);
            stringbuffer.append(ac[(i3 << 2 & 0x3F) | k3 >> 6]);
            stringbuffer.append(ac[k3 & 0x3F]);
        }
        if (k != 0) {
            final int l2 = bb[i2++] & 0xFF;
            stringbuffer.append(ac[l2 >> 2]);
            if (k == 1) {
                stringbuffer.append(ac[l2 << 4 & 0x3F]);
                stringbuffer.append("==");
            }
            else {
                final int j3 = bb[i2++] & 0xFF;
                stringbuffer.append(ac[(l2 << 4 & 0x3F) | j3 >> 4]);
                stringbuffer.append(ac[j3 << 2 & 0x3F]);
                stringbuffer.append('=');
            }
        }
        return stringbuffer.toString();
    }
    
    public static String byteArrayToBase64(final byte[] bb) {
        return _$23170(bb, false);
    }
    
    public static void main(final String[] args) {
        String s = "0123456789";
        byte[] b = s.getBytes();
        s = byteArrayToBase64(b);
        System.out.println(s);
        b = base64ToByteArray(s);
        System.out.println(new String(b));
    }
    
    static {
        _$23169 = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25 };
        _$23168 = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        _$23167 = new char[] { '!', '\"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?' };
        _$23166 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
    }
}
