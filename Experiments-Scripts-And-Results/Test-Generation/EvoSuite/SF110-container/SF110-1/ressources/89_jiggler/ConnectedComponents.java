package jigl.image.ops;

import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponents extends SimpleOperator
{
    public static final int FOUR_CONNECTED = 0;
    public static final int EIGHT_CONNECTED = 1;
    private int neighbors;
    private int background;
    
    public ConnectedComponents(final int numberNeighbors, final int backgroundColor) throws InvalidArgumentException {
        this.neighbors = 0;
        this.background = 0;
        this.neighbors = numberNeighbors;
        this.background = backgroundColor;
        if ((this.neighbors != 0 && this.neighbors != 1) || (this.background != 1 && this.background != 0)) {
            throw new InvalidArgumentException();
        }
    }
    
    @Override
    protected GrayImage apply(final BinaryImage image) {
        final int[] equiv = new int[100000];
        int current = 1;
        int min = 1000;
        final int[] friends = new int[4];
        final GrayImage gimage = new GrayImage(image.X(), image.Y());
        for (int x = 0; x < 1000; ++x) {
            equiv[x] = -1;
        }
        for (int x = 0; x < image.X(); ++x) {
            for (int y = 0; y < image.Y(); ++y) {
                if (image.get(x, y) == this.background) {
                    gimage.set(x, y, Integer.valueOf(0));
                }
                else if (x == 0 && y == 0) {
                    gimage.set(0, 0, Integer.valueOf(current));
                    ++current;
                }
                else {
                    if (x - 1 == -1) {
                        friends[1] = (friends[0] = -1);
                        friends[2] = gimage.get(x, y - 1);
                        if (x + 1 >= image.X()) {
                            friends[3] = -1;
                        }
                        else {
                            friends[3] = gimage.get(x + 1, y - 1);
                        }
                    }
                    else if (y - 1 == -1) {
                        friends[0] = gimage.get(x - 1, y);
                        friends[1] = -1;
                        friends[3] = (friends[2] = -1);
                    }
                    else {
                        friends[0] = gimage.get(x - 1, y);
                        friends[1] = gimage.get(x - 1, y - 1);
                        friends[2] = gimage.get(x, y - 1);
                        if (x + 1 >= image.X()) {
                            friends[3] = -1;
                        }
                        else {
                            friends[3] = gimage.get(x + 1, y - 1);
                        }
                    }
                    if (this.neighbors == 0) {
                        if (friends[0] <= 0 && friends[2] <= 0) {
                            gimage.set(x, y, Integer.valueOf(current));
                            ++current;
                        }
                        else if (friends[0] <= 0) {
                            gimage.set(x, y, Integer.valueOf(friends[2]));
                        }
                        else if (friends[2] <= 0) {
                            gimage.set(x, y, Integer.valueOf(friends[0]));
                        }
                        else if (friends[0] < friends[2]) {
                            gimage.set(x, y, Integer.valueOf(friends[0]));
                            equiv[friends[2]] = friends[0];
                        }
                        else {
                            gimage.set(x, y, Integer.valueOf(friends[2]));
                            if (friends[0] != friends[2]) {
                                equiv[friends[0]] = friends[2];
                            }
                        }
                    }
                    else if (this.neighbors == 1) {
                        if (friends[0] <= 0 && friends[1] <= 0 && friends[2] <= 0 && friends[3] <= 0) {
                            gimage.set(x, y, Integer.valueOf(current));
                            ++current;
                        }
                        else {
                            int minVal = 1000;
                            for (int z = 0; z < 4; ++z) {
                                if (friends[z] < minVal && friends[z] != 0 && friends[z] != -1) {
                                    min = z;
                                    minVal = friends[z];
                                }
                            }
                            gimage.set(x, y, Integer.valueOf(friends[min]));
                            for (int z = 0; z < 4; ++z) {
                                if (friends[z] != friends[min] && friends[z] != 0 && friends[z] != -1) {
                                    equiv[friends[z]] = friends[min];
                                }
                            }
                        }
                    }
                }
            }
        }
        int curVal = 0;
        for (int x2 = 0; x2 < gimage.X(); ++x2) {
            for (int y2 = 0; y2 < gimage.Y(); ++y2) {
                for (curVal = gimage.get(x2, y2); equiv[curVal] != -1; curVal = equiv[curVal]) {}
                gimage.set(x2, y2, Integer.valueOf(curVal));
            }
        }
        return gimage;
    }
}
