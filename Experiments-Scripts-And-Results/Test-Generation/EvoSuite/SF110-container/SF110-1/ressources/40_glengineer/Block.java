package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;

public abstract class Block extends CharTable
{
    protected Scheme scheme;
    
    public Block(final Scheme scheme, final int x1, final int y1, final int x2, final int y2) {
        if (x1 < 0 || y1 < 0 || x1 > x2 || y1 > y2 || x2 > scheme.x2 || y2 > scheme.y2) {
            throw new IllegalArgumentException("improper block definition: " + x1 + "," + y1 + ", " + x2 + "," + y2 + ".");
        }
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.scheme = scheme;
    }
    
    public Block(final Scheme scheme, final CharPosition p1, final CharPosition p2) {
        this(scheme, p1.x, p1.y, p2.x, p2.y);
    }
    
    public Block(final Scheme scheme) {
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = scheme.x2;
        this.y2 = scheme.y2;
        this.scheme = scheme;
    }
    
    public abstract Block subblock(final int p0, final int p1, final int p2, final int p3);
    
    @Override
    public char charAt(final int x, final int y) {
        try {
            return this.scheme.lines[y].charAt(x);
        }
        catch (RuntimeException re) {
            throw new IllegalArgumentException("char position " + new CharPosition(x, y) + " out of block.\n" + this);
        }
    }
    
    @Override
    public String textAt(final int x1, final int x2, final int y) {
        try {
            return this.scheme.lines[y].substring(x1, x2);
        }
        catch (RuntimeException re) {
            throw new IllegalArgumentException("Improper subline coodinates.");
        }
    }
    
    public boolean containsOnlyOneWord() {
        if (this.isTrivial()) {
            throw new IllegalArgumentException("cannot search in a trivial block");
        }
        final HWordPosition firstWord = this.firstWord_Horizontally();
        if (firstWord.y == this.y2) {
            throw new IllegalArgumentException("cannot verify unicity of an word in an empty block");
        }
        final WordPosition lastWord = this.lastWord_Horizontally();
        return firstWord.equals(lastWord);
    }
    
    public abstract List<Block> extractSequentialSubblocks();
    
    public abstract List<Block> extractParallelSubblocks();
    
    @Override
    public String toString() {
        String result = "Block(" + this.x1 + "," + this.y1 + ", " + this.x2 + "," + this.y2 + "):\n";
        final int frameWidth = this.x2 - this.x1 + 4;
        final char[] line = new char[frameWidth];
        Arrays.fill(line, '#');
        final String lineString = " " + new String(line);
        result = result.concat(lineString + "\n");
        for (int y = this.y1; y < this.y2; ++y) {
            result = result.concat(" # " + this.scheme.textAt(this.x1, this.x2, y) + " #\n");
        }
        result = result.concat(lineString);
        return result;
    }
    
    public String getUpperLine() {
        return this.textAt(this.x1, this.x2, this.y1);
    }
}
