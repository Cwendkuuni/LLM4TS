package jigl.image.ops.levelOps;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class Clip extends SimpleOperator
{
    private int int_min;
    private int int_max;
    private float float_min;
    private float float_max;
    
    public Clip(final int min, final int max) {
        this.int_min = min;
        this.int_max = max;
    }
    
    public Clip(final float min, final float max) {
        this.float_min = min;
        this.float_max = max;
    }
    
    @Override
    protected Image apply(final GrayImage image) {
        return this.apply(image, new ROI(0, 0, image.X() - 1, image.Y() - 1));
    }
    
    @Override
    protected Image apply(final GrayImage image, final ROI roi) {
        if (this.int_min == 0 && this.int_max == 0) {
            this.int_min = (int)this.float_min;
            this.int_max = (int)this.float_max;
        }
        int value = 0;
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                value = image.get(x, y);
                value = ((value > this.int_max) ? this.int_max : value);
                value = ((value < this.int_min) ? this.int_min : value);
                image.set(x, y, Integer.valueOf(value));
            }
        }
        return image;
    }
    
    @Override
    protected Image apply(final RealGrayImage image) {
        return this.apply(image, new ROI(0, 0, image.X() - 1, image.Y() - 1));
    }
    
    @Override
    protected Image apply(final RealGrayImage image, final ROI roi) {
        if (this.float_min == 0.0f && this.float_max == 0.0f) {
            this.float_min = this.int_min;
            this.float_max = this.int_max;
        }
        float value = 0.0f;
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                value = image.get(x, y);
                value = ((value > this.float_max) ? this.float_max : value);
                value = ((value < this.float_min) ? this.float_min : value);
                image.set(x, y, Float.valueOf(value));
            }
        }
        return image;
    }
    
    @Override
    protected Image apply(final ColorImage image) {
        image.setPlane(0, (GrayImage)this.apply(image.plane(0), new ROI(0, 0, image.X() - 1, image.Y() - 1)));
        image.setPlane(1, (GrayImage)this.apply(image.plane(1), new ROI(0, 0, image.X() - 1, image.Y() - 1)));
        image.setPlane(2, (GrayImage)this.apply(image.plane(2), new ROI(0, 0, image.X() - 1, image.Y() - 1)));
        return image;
    }
    
    @Override
    protected Image apply(final ColorImage image, final ROI roi) {
        image.setPlane(0, (GrayImage)this.apply(image.plane(0), roi));
        image.setPlane(1, (GrayImage)this.apply(image.plane(1), roi));
        image.setPlane(2, (GrayImage)this.apply(image.plane(2), roi));
        return image;
    }
    
    @Override
    protected Image apply(final RealColorImage image) {
        image.setPlane(0, (RealGrayImage)this.apply(image.plane(0), new ROI(0, 0, image.X() - 1, image.Y() - 1)));
        image.setPlane(1, (RealGrayImage)this.apply(image.plane(1), new ROI(0, 0, image.X() - 1, image.Y() - 1)));
        image.setPlane(2, (RealGrayImage)this.apply(image.plane(2), new ROI(0, 0, image.X() - 1, image.Y() - 1)));
        return image;
    }
    
    @Override
    protected Image apply(final RealColorImage image, final ROI roi) {
        image.setPlane(0, (RealGrayImage)this.apply(image.plane(0), roi));
        image.setPlane(1, (RealGrayImage)this.apply(image.plane(1), roi));
        image.setPlane(2, (RealGrayImage)this.apply(image.plane(2), roi));
        return image;
    }
    
    public static void main(final String[] argv) {
        try {
            Image image = null;
            final String inputfile = argv[2];
            Image image2 = null;
            final ImageInputStream is = new ImageInputStream(inputfile);
            image = is.read();
            is.close();
            final Float f_val1 = Float.valueOf(argv[0]);
            final Float f_val2 = Float.valueOf(argv[1]);
            final float val1 = f_val1;
            final float val2 = f_val2;
            final Clip clip = new Clip(val1, val2);
            image2 = clip.apply(image);
            final ImageOutputStream os = new ImageOutputStream(argv[3]);
            os.write(image2);
            os.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
