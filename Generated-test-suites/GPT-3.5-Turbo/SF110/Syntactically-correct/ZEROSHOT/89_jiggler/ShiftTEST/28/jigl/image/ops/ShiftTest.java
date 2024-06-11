package jigl.image.ops;

import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void apply_ShouldShiftGrayImageWithWrap() {
        Shift shift = new Shift(1, 2, 1);
        int[][] data = {{1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}};
        GrayImage image = new GrayImage(data);
        GrayImage result = shift.apply(image);
        int[][] expected = {{9, 7, 8},
                            {3, 1, 2},
                            {6, 4, 5}};
        assertArrayEquals(expected, result.getData());
    }

    @Test
    public void apply_ShouldShiftGrayImageWithoutWrap() {
        Shift shift = new Shift(1, 2, 0);
        int[][] data = {{1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}};
        GrayImage image = new GrayImage(data);
        GrayImage result = shift.apply(image);
        int[][] expected = {{0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0}};
        assertArrayEquals(expected, result.getData());
    }

    @Test
    public void apply_ShouldShiftRealGrayImageWithWrap() {
        Shift shift = new Shift(1, 2, 1);
        float[][] data = {{1.1f, 2.2f, 3.3f},
                          {4.4f, 5.5f, 6.6f},
                          {7.7f, 8.8f, 9.9f}};
        RealGrayImage image = new RealGrayImage(data);
        RealGrayImage result = shift.apply(image);
        float[][] expected = {{9.9f, 7.7f, 8.8f},
                              {3.3f, 1.1f, 2.2f},
                              {6.6f, 4.4f, 5.5f}};
        assertArrayEquals(expected, result.getData());
    }

    @Test
    public void apply_ShouldShiftRealGrayImageWithoutWrap() {
        Shift shift = new Shift(1, 2, 0);
        float[][] data = {{1.1f, 2.2f, 3.3f},
                          {4.4f, 5.5f, 6.6f},
                          {7.7f, 8.8f, 9.9f}};
        RealGrayImage image = new RealGrayImage(data);
        RealGrayImage result = shift.apply(image);
        float[][] expected = {{0.0f, 0.0f, 0.0f},
                              {0.0f, 0.0f, 0.0f},
                              {0.0f, 0.0f, 0.0f}};
        assertArrayEquals(expected, result.getData());
    }

    @Test
    public void apply_ShouldShiftColorImageWithWrap() {
        Shift shift = new Shift(1, 2, 1);
        int[][][] data = {{{1, 2, 3},
                            {4, 5, 6},
                            {7, 8, 9}},
                           {{10, 11, 12},
                            {13, 14, 15},
                            {16, 17, 18}},
                           {{19, 20, 21},
                            {22, 23, 24},
                            {25, 26, 27}}};
        ColorImage image = new ColorImage(data);
        ColorImage result = shift.apply(image);
        int[][][] expected = {{{9, 7, 8},
                               {3, 1, 2},
                               {6, 4, 5}},
                              {{18, 16, 17},
                               {12, 10, 11},
                               {15, 13, 14}},
                              {{27, 25, 26},
                               {21, 19, 20},
                               {24, 22, 23}}};
        assertArrayEquals(expected, result.getData());
    }

    @Test
    public void apply_ShouldShiftRealColorImageWithWrap() {
        Shift shift = new Shift(1, 2, 1);
        float[][][] data = {{{1.1f, 2.2f, 3.3f},
                             {4.4f, 5.5f, 6.6f},
                             {7.7f, 8.8f, 9.9f}},
                            {{10.1f, 11.2f, 12.3f},
                             {13.4f, 14.5f, 15.6f},
                             {16.7f, 17.8f, 18.9f}},
                            {{19.1f, 20.2f, 21.3f},
                             {22.4f, 23.5f, 24.6f},
                             {25.7f, 26.8f, 27.9f}}};
        RealColorImage image = new RealColorImage(data);
        RealColorImage result = shift.apply(image);
        float[][][] expected = {{{9.9f, 7.7f, 8.8f},
                                 {3.3f, 1.1f, 2.2f},
                                 {6.6f, 4.4f, 5.5f}},
                                {{18.9f, 16.7f, 17.8f},
                                 {12.3f, 10.1f, 11.2f},
                                 {15.6f, 13.4f, 14.5f}},
                                {{27.9f, 25.7f, 26.8f},
                                 {21.3f, 19.1f, 20.2f},
                                 {24.6f, 22.4f, 23.5f}}};
        assertArrayEquals(expected, result.getData());
    }

}