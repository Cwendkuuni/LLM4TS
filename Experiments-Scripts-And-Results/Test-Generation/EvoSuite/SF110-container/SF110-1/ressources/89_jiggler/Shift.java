package jigl.image.ops;

import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class Shift extends SimpleOperator
{
    public static final int NOWRAP = 0;
    public static final int WRAP = 1;
    private int xoffset;
    private int yoffset;
    private boolean wrap;
    
    public Shift(final int x, final int y, final int wrap) {
        this.xoffset = 0;
        this.yoffset = 0;
        this.wrap = false;
        this.xoffset = x;
        this.yoffset = y;
        if (wrap == 1) {
            this.wrap = true;
        }
    }
    
    @Override
    protected GrayImage apply(final GrayImage image) {
        final int X = image.X();
        final int Y = image.Y();
        final GrayImage temp = new GrayImage(X, Y);
        if (this.wrap) {
            for (int y = 0; y < Y; ++y) {
                for (int x = 0; x < X; ++x) {
                    if (this.xoffset > 0 && this.yoffset > 0) {
                        temp.set((x + this.xoffset) % X, (y + this.yoffset) % Y, image.get(x, y));
                    }
                    else if (this.xoffset > 0) {
                        temp.set((x + this.xoffset) % X, (Y + this.yoffset + y) % Y, image.get(x, y));
                    }
                    else if (this.yoffset > 0) {
                        temp.set((X + this.xoffset + x) % X, (y + this.yoffset) % Y, image.get(x, y));
                    }
                    else {
                        temp.set((X + this.xoffset + x) % X, (y + this.yoffset + Y) % Y, image.get(x, y));
                    }
                }
            }
        }
        else {
            for (int y = 0; y < Y; ++y) {
                for (int x = 0; x < X; ++x) {
                    if (x + this.xoffset >= 0 && x + this.xoffset < X && y + this.yoffset >= 0 && y + this.yoffset < Y) {
                        temp.set(x, y, image.get(x + this.xoffset, y + this.yoffset));
                    }
                    else {
                        temp.set(x, y, Integer.valueOf(0));
                    }
                }
            }
        }
        return temp;
    }
    
    @Override
    protected RealGrayImage apply(final RealGrayImage image) {
        final int X = image.X();
        final int Y = image.Y();
        final RealGrayImage temp = new RealGrayImage(X, Y);
        if (this.wrap) {
            for (int y = 0; y < Y; ++y) {
                for (int x = 0; x < X; ++x) {
                    if (this.xoffset > 0 && this.yoffset > 0) {
                        temp.set((x + this.xoffset) % X, (y + this.yoffset) % Y, image.get(x, y));
                    }
                    else if (this.xoffset > 0) {
                        temp.set((x + this.xoffset) % X, (Y + this.yoffset + y) % Y, image.get(x, y));
                    }
                    else if (this.yoffset > 0) {
                        temp.set((X + this.xoffset + x) % X, (y + this.yoffset) % Y, image.get(x, y));
                    }
                    else {
                        temp.set((X + this.xoffset + x) % X, (y + this.yoffset + Y) % Y, image.get(x, y));
                    }
                }
            }
        }
        else {
            for (int y = 0; y < Y; ++y) {
                for (int x = 0; x < X; ++x) {
                    if (x + this.xoffset >= 0 && x + this.xoffset < X && y + this.yoffset >= 0 && y + this.yoffset < Y) {
                        temp.set(x, y, image.get(x + this.xoffset, y + this.yoffset));
                    }
                    else {
                        temp.set(x, y, Float.valueOf(0.0f));
                    }
                }
            }
        }
        return temp;
    }
    
    @Override
    protected ColorImage apply(final ColorImage image) {
        final ColorImage temp = new ColorImage(image.X(), image.Y());
        temp.setPlane(0, this.apply(image.plane(0)));
        temp.setPlane(1, this.apply(image.plane(1)));
        temp.setPlane(2, this.apply(image.plane(2)));
        return temp;
    }
    
    @Override
    protected RealColorImage apply(final RealColorImage image) {
        final RealColorImage temp = new RealColorImage(image.X(), image.Y());
        temp.setPlane(0, this.apply(image.plane(0)));
        temp.setPlane(1, this.apply(image.plane(1)));
        temp.setPlane(2, this.apply(image.plane(2)));
        return temp;
    }
}
