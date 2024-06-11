package net.sf.lavalamp.device;

public class TimeOfDay implements Comparable<TimeOfDay>
{
    private Integer hours;
    private Integer minutes;
    
    public TimeOfDay(final int hours, final int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }
    
    public TimeOfDay(final String time) {
        this.hours = new Integer(time.substring(0, 2));
        this.minutes = new Integer(time.substring(3, 5));
    }
    
    @Override
    public int compareTo(final TimeOfDay that) {
        final int hourComparison = this.hours.compareTo(that.hours);
        if (hourComparison != 0) {
            return hourComparison;
        }
        return this.minutes.compareTo(that.minutes);
    }
    
    public Integer getHours() {
        return this.hours;
    }
    
    public Integer getMinutes() {
        return this.minutes;
    }
    
    @Override
    public String toString() {
        return this.hours + ":" + this.minutes;
    }
    
    @Override
    public int hashCode() {
        return new Integer(100 * this.hours + this.minutes).hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final TimeOfDay other = (TimeOfDay)obj;
        return this.hours.equals(other.hours) && this.minutes.equals(other.minutes);
    }
}
