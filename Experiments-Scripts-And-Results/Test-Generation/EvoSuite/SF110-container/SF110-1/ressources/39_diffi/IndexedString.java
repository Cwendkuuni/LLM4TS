package de.beiri22.stringincrementor.helper;

public final class IndexedString
{
    private char[] values;
    private int[][] index;
    
    private int count(final char c) {
        int result = 0;
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] == c) {
                ++result;
            }
        }
        return result;
    }
    
    public IndexedString(final String s) {
        this.values = new char[s.length()];
        s.getChars(0, s.length(), this.values, 0);
        this.index = new int[256][];
        for (char c = '\0'; c < '\u0100'; ++c) {
            this.index[c] = new int[this.count(c)];
            int idx = 0;
            for (int i = 0; i < this.values.length; ++i) {
                if (this.values[i] == c) {
                    this.index[c][idx++] = i;
                }
            }
        }
    }
    
    public int indexOf(final char[] target) {
        final char first = target[0];
        final int tl = target.length;
        final int max = this.values.length - tl;
        for (final int i : this.index[first]) {
            if (i <= max) {
                int j = i + 1;
                final int end = i + tl;
                for (int k = 1; j < end && this.values[j] == target[k]; ++j, ++k) {}
                if (j == end) {
                    return i;
                }
            }
        }
        return -1;
    }
}
