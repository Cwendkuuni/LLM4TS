package org.jcvi.jillion.assembly.ca.frg;

public final class Distance
{
    private static final float SIX = 6.0f;
    private static final int CA_GATEKEEPER_STDDEV_LIMIT = 3;
    private static final float MAX_ROUNDING_ERROR = 1.015f;
    private final int min;
    private final int max;
    private final float mean;
    private final float stdDev;
    
    public static Distance buildDistance(final int min, final int max, final float mean, final float stdDev) {
        return new Distance(min, max, mean, stdDev);
    }
    
    public static Distance buildDistance(final float mean, final float stdDev) {
        final float delta = calculateDelta(stdDev);
        final int plusDelta = (int)(mean + delta);
        final int minusDelta = (int)(mean - delta);
        return new Distance(Math.min(plusDelta, minusDelta), Math.max(plusDelta, minusDelta), mean, stdDev);
    }
    
    private static float calculateDelta(final float stdDev) {
        return 3.0f * stdDev;
    }
    
    public static Distance buildDistance(final int min, final int max) {
        final float mean = computeMean(min, max);
        final float stdDev = computeStandardDeviation(min, max);
        return new Distance(min, max, mean, stdDev);
    }
    
    private static float computeStandardDeviation(final int min, final int max) {
        return (max - min) / 6.0f;
    }
    
    private static float computeMean(final int min, final int max) {
        return min / 2.0f + max / 2.0f;
    }
    
    public static Distance transformIntoCeleraAssemblerDistance(final Distance distance) {
        final float mean = distance.getMean();
        final float stdDev = distance.getStdDev();
        if (mean < calculateDelta(stdDev)) {
            final float correctedStdDev = (mean - 1.015f) / 3.0f;
            return new Distance(distance.getMin(), distance.getMax(), mean, correctedStdDev);
        }
        return distance;
    }
    
    private Distance(final int min, final int max, final float mean, final float stdDev) {
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.stdDev = stdDev;
    }
    
    public float getMean() {
        return this.mean;
    }
    
    public float getStdDev() {
        return this.stdDev;
    }
    
    public int getMin() {
        return this.min;
    }
    
    public int getMax() {
        return this.max;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.max;
        result = 31 * result + Float.floatToIntBits(this.mean);
        result = 31 * result + this.min;
        result = 31 * result + Float.floatToIntBits(this.stdDev);
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Distance)) {
            return false;
        }
        final Distance other = (Distance)obj;
        return this.max == other.max && Float.floatToIntBits(this.mean) == Float.floatToIntBits(other.mean) && this.min == other.min && Float.floatToIntBits(this.stdDev) == Float.floatToIntBits(other.stdDev);
    }
    
    @Override
    public String toString() {
        return String.format("%.3f, std: %.3f", this.mean, this.stdDev);
    }
}
